package com.adnovum.vcms.issuer.controller;

import com.adnovum.vcms.common.datamodel.event.CredentialExchangeEvent;
import com.adnovum.vcms.common.datamodel.event.CredentialExchangeState;
import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.issuer.server.dto.CredentialResponse;
import com.adnovum.vcms.genapi.issuer.server.dto.HolderResponse;
import com.adnovum.vcms.issuer.IssuerServerIntTestBase;
import com.adnovum.vcms.issuer.datamodel.entity.IssuingProcess;
import com.adnovum.vcms.issuer.datamodel.enumeration.ProcessState;
import com.adnovum.vcms.issuer.datamodel.enumeration.RevocationState;
import com.adnovum.vcms.issuer.listener.CredentialExchangeListener;
import com.adnovum.vcms.issuer.listener.ListenerProperties;
import com.adnovum.vcms.issuer.service.AriesFacadeService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class AdminControllerTest extends IssuerServerIntTestBase {

	@Test
	void getCredentialsFromHolder() {
		String test = "getCredentialsFromHolder";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		AriesFacadeClient ariesFacadeClient = mock(AriesFacadeClient.class);
		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClient);

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		issuingProcess = issuingProcessService.updateRevocationState(issuingProcess.getId(), RevocationState.ISSUED);
		AdminController adminController = new AdminController(holderService, issuingProcessService, ariesFacadeService);

		List<CredentialResponse> credentialResponses =
				adminController.getCredentialsFromHolder(issuingProcess.getHolder().getId()).getBody(); // FIXME mapping fials

		assertThat(credentialResponses).hasSize(1);
		assert credentialResponses != null;
		assertThat(credentialResponses.get(0).getConnectionId()).isEqualTo(issuingProcess.getConnectionId());
		assertThat(credentialResponses.get(0).getCredentialId()).isEqualTo(issuingProcess.getId().toString());
		assertThat(credentialResponses.get(0).getRevocationState()).isEqualTo(issuingProcess.getRevocationState().getValue());
		assertThat(credentialResponses.get(0).getProcessState()).isEqualTo(issuingProcess.getProcessState().getValue());
	}

	@Test
	void getHolders() {
		String test = "getHolders";
		createHolder(test + "_holder1");
		createHolder(test + "_holder2");

		AriesFacadeClient ariesFacadeClient = mock(AriesFacadeClient.class);
		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClient);
		AdminController adminController = new AdminController(holderService, issuingProcessService, ariesFacadeService);

		List<HolderResponse> holders = adminController.getHolders().getBody();
		assertThat(holders).hasSize(2);
		assertThat(holders.stream().map(HolderResponse::getUserId).collect(Collectors.toList()))
				.contains(test + "_holder1", test + "_holder2");
	}

	@Test
	void deleteHolder() {
		String test = "deleteHolder";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);

		AriesFacadeClient ariesFacadeClient = mock(AriesFacadeClient.class);
		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClient);
		AdminController adminController = new AdminController(holderService, issuingProcessService, ariesFacadeService);

		issuingProcess = issuingProcessService.updateRevocationState(issuingProcess.getId(), RevocationState.ISSUED);
		UUID holderId = issuingProcess.getHolder().getId();
		assertThatThrownBy(() -> adminController.deleteHolder(holderId))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Holder can not be deleted because it has issued credentials.");
		assertThat(holderService.getHolderById(holderId).getUserId()).isEqualTo(test);

		issuingProcess = issuingProcessService.updateRevocationState(issuingProcess.getId(), RevocationState.REVOKED);
		adminController.deleteHolder(issuingProcess.getHolder().getId());
		assertThatThrownBy(() -> holderService.getHolderById(holderId))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Cannot resolve the given id = " + holderId);
	}

	@Test
	void revokeCredential() {
		// Arrange
		String test = "revokeCredential";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		// Create an issuing process to revoke
		IssuingProcess issuingProcess = createIssuingProcess(connectionId, credentialExchangeId, test);
		AriesFacadeClient ariesFacadeClient = mock(AriesFacadeClient.class);
		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClient);
		AdminController adminController = new AdminController(holderService, issuingProcessService, ariesFacadeService);

		// Mock the webhook call coming from AcaPy
		ListenerProperties listenerProperties =  new ListenerProperties();
		listenerProperties.setQueues(Collections.EMPTY_MAP);
		CredentialExchangeListener listener = new CredentialExchangeListener(issuingProcessService, listenerProperties,
				ariesFacadeService);
		CredentialExchangeEvent credentialExchangeEvent = new CredentialExchangeEvent();
		credentialExchangeEvent.setConnectionId(connectionId);
		credentialExchangeEvent.setCredentialExchangeId(credentialExchangeId);
		credentialExchangeEvent.setCredentialExchangeState(CredentialExchangeState.CREDENTIAL_REVOKED);

		// Act
		// Human trigger for revocation
		adminController.revokeCredential(issuingProcess.getId());

		// Simulate AcaPy response
		listener.receiveCredentialExchangeEvent(credentialExchangeEvent);

		// Assert
		issuingProcess = issuingProcessService.getIssuingProcessById(issuingProcess.getId());
		assertThat(issuingProcess.getRevocationState()).isEqualTo(RevocationState.REVOKED);
		assertThat(issuingProcess.getProcessState()).isEqualTo(ProcessState.CREDENTIAL_REVOKED);

	}
}
