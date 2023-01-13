package com.adnovum.vcms.issuer.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.client.dto.IssuingResponse;
import com.adnovum.vcms.genapi.issuer.server.dto.IssuStatus;
import com.adnovum.vcms.genapi.issuer.server.dto.IssuingRequest;
import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.Claim;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;
import com.adnovum.vcms.issuer.service.AriesFacadeService;
import org.junit.jupiter.api.Test;

class IssuerControllerTest extends IssuerServerIntTestBase {

	@Test
	void issueCredential() {
		String test = "issueCredential";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId1 = UUID.randomUUID().toString();
		String credentialExchangeId2 = UUID.randomUUID().toString();

		AriesFacadeClient ariesFacadeClientMock = mock(AriesFacadeClient.class);
		when(ariesFacadeClientMock.getConnectionState(connectionId))
				.thenReturn(AriesConnectionState.CREATED)
				.thenReturn(AriesConnectionState.REQUESTED)
				.thenReturn(AriesConnectionState.RESPONDED)
				.thenReturn(AriesConnectionState.ESTABLISHED);

		when(ariesFacadeClientMock.isCreatedCreDef(any())).thenReturn(Boolean.TRUE);

		IssuingResponse issuingResponse1 = new IssuingResponse();
		issuingResponse1.setCredentialExchangeId(credentialExchangeId1);
		IssuingResponse issuingResponse2 = new IssuingResponse();
		issuingResponse2.setCredentialExchangeId(credentialExchangeId2);
		when(ariesFacadeClientMock.offerCredential(any(), any()))
				.thenReturn(issuingResponse1)
				.thenReturn(issuingResponse2);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClientMock);
		IssuerController issuerController =
				new IssuerController(issuingProcessService, ariesFacadeService, holderService, claimService);

		// connection not ready: CREATED
		IssuingRequest issuingRequest = new IssuingRequest();
		issuingRequest.setConnectionId(connectionId);
		IssuStatus issuStatus = Objects.requireNonNull(issuerController.issueCredential(issuingRequest).getBody()).getStatus();
		assertThat(issuStatus).isEqualTo(IssuStatus.NO_CONNECTION);
		assertThat(issuingProcessRepository.findAll()).isEmpty();

		// connection not ready: REQUESTED
		issuStatus = Objects.requireNonNull(issuerController.issueCredential(issuingRequest).getBody()).getStatus();
		assertThat(issuStatus).isEqualTo(IssuStatus.NO_CONNECTION);
		assertThat(issuingProcessRepository.findAll()).isEmpty();

		// connection ready: RESPONDED
		issuingRequest.setUserId(test);
		issuingRequest.setAttributes(Map.of(
				test + "_key1", test + "_value1",
				test + "_key2", test + "_value2",
				test + "_key3", test + "_value3")
		);
		issuStatus = Objects.requireNonNull(issuerController.issueCredential(issuingRequest).getBody()).getStatus();
		assertThat(issuStatus).isEqualTo(IssuStatus.IN_PROGRESS);
		IssuingProcess issuingProcess =
				issuingProcessService.getOptionalByCredentialExchangeId(credentialExchangeId1).orElseThrow();
		assertThat(issuingProcess.getProcessState()).isEqualTo(ProcessState.PROCESS_CREATED);
		assertThat(issuingProcess.getHolder().getUserId()).isEqualTo(test);

		Set<Claim> claims = claimRepository.findAllByIssuingProcessId(issuingProcess.getId());
		assertThat(claims).hasSize(3);

		// connection ready: ESTABLISHED
		issuStatus = Objects.requireNonNull(issuerController.issueCredential(issuingRequest).getBody()).getStatus();
		assertThat(issuStatus).isEqualTo(IssuStatus.IN_PROGRESS);
		issuingProcess = issuingProcessService.getOptionalByCredentialExchangeId(credentialExchangeId2).orElseThrow();
		assertThat(issuingProcess.getProcessState()).isEqualTo(ProcessState.PROCESS_CREATED);
		assertThat(issuingProcess.getHolder().getUserId()).isEqualTo(test);

		assertThat(issuingProcessRepository.findAll()).hasSize(2);
		assertThat(holderRepository.findAll()).hasSize(1);
	}

	@Test
	void cannotIssueWithoutCreatedCreDef() {
		String test = "cannotIssueWithoutCreatedCreDef";
		UUID connectionId = UUID.randomUUID();

		AriesFacadeClient ariesFacadeClient = mock(AriesFacadeClient.class);
		when(ariesFacadeClient.getConnectionState(connectionId)).thenReturn(AriesConnectionState.ESTABLISHED);
		when(ariesFacadeClient.isCreatedCreDef(test)).thenReturn(Boolean.FALSE);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClient);
		IssuerController issuerController =
				new IssuerController(issuingProcessService, ariesFacadeService, holderService, claimService);
		IssuingRequest issuingRequest = new IssuingRequest();
		issuingRequest.setCredentialDefinitionId(test);
		issuingRequest.setConnectionId(connectionId);
		assertThatThrownBy(() -> issuerController.issueCredential(issuingRequest))
				.isInstanceOf(BusinessException.class)
				.hasMessage("The given credential definition " + test + " has not been created by this wallet and can therefore "
						+ "not be used for issuing.");
	}

	@Test
	void issuingState() {
		String test = "issuingState";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();
		UUID randomProcessId = UUID.randomUUID();

		AriesFacadeClient controllerClientMock = mock(AriesFacadeClient.class);
		AriesFacadeService ariesFacadeService = new AriesFacadeService(controllerClientMock);
		IssuerController issuerController =
				new IssuerController(issuingProcessService, ariesFacadeService, holderService, claimService);

		assertThatThrownBy(() -> issuerController.issuingState(randomProcessId))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Cannot resolve the given {processId} = " + randomProcessId);

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		assertThat(issuerController.issuingState(issuingProcess.getId()).getBody()).isEqualTo(IssuStatus.IN_PROGRESS);
	}
}
