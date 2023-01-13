package com.adnovum.vcms.verifier.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.client.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.verifier.server.dto.VerStatus;
import com.adnovum.vcms.genapi.verifier.server.dto.VerificationRequest;
import com.adnovum.vcms.genapi.verifier.server.dto.VerificationResponse;
import com.adnovum.vcms.verifier.VerifierServerIntBase;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.enumeration.VerificationProcessState;
import com.adnovum.vcms.verifier.service.AriesFacadeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class VerificationControllerTest extends VerifierServerIntBase {

	private static AriesFacadeService ariesFacadeServiceMock;

	private static AriesFacadeClient ariesFacadeClientMock;


	@BeforeAll
	static void setupMocks() {
		ariesFacadeServiceMock = mock(AriesFacadeService.class);
		ariesFacadeClientMock = mock(AriesFacadeClient.class);
	}

	@Test
	void shouldGetStatusUnknownProcessId() {
		UUID processId = UUID.randomUUID();

		VerificationController verificationController = new VerificationController(verificationProcessService,
				verifiedClaimService, ariesFacadeServiceMock);
		assertThatThrownBy(() -> verificationController.getVerificationState(processId))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Cannot resolve the given {id} = " + processId);
	}

	@Test
	void shouldGetStatusVerified() {
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = createVerificationProcess(connectionId, exchangeId);
		verificationProcess.setStatus(VerificationProcessState.PRESENTATION_VERIFIED);
		verificationProcessRepository.save(verificationProcess);

		VerificationController verificationController = new VerificationController(verificationProcessService,
				verifiedClaimService,
				ariesFacadeServiceMock);
		VerStatus verStatus = verificationController.getVerificationState(verificationProcess.getId()).getBody();
		assertThat(verStatus).isNotNull();
		assertThat(verStatus.getValue()).isEqualTo(VerStatus.VERIFIED.getValue());
	}

	@Test
	void getCredentialDefinitions() {
		String test = "getCredentialDefinitions";

		CredentialDefinition credentialDefinition = new CredentialDefinition();
		credentialDefinition.setCredentialDefinitionId(test);
		credentialDefinition.setClaims(List.of(test + "_11", test + "_12", test + "_13"));
		CredentialDefinition credentialDefinition2 = new CredentialDefinition();
		credentialDefinition2.setCredentialDefinitionId(test + "_2");
		credentialDefinition2.setClaims(List.of(test + "_21", test + "_22", test + "_23"));
		when(ariesFacadeClientMock.getCredentialDefinitionsById(any())).thenReturn(
				List.of(credentialDefinition, credentialDefinition2));

		AriesFacadeService controllerService = new AriesFacadeService(verifierConfiguration, ariesFacadeClientMock);
		VerificationController verificationController =
				new VerificationController(verificationProcessService, verifiedClaimService,
						controllerService);

		List<com.adnovum.vcms.genapi.verifier.server.dto.CredentialDefinition> credentialDefinitions
				= verificationController.getCredentialDefinitions().getBody();

		assertThat(credentialDefinitions).hasSize(2);
	}

	@Test
	void getVerifiedClaims() {
		String connectionId = UUID.randomUUID().toString();
		String exchangeId = UUID.randomUUID().toString();

		createVerifiedClaimRecords(connectionId, exchangeId);
		VerificationProcess verificationProcess =
				verificationProcessRepository.findByPresentationExchangeId(exchangeId).orElseThrow();

		AriesFacadeService ariesFacadeService = new AriesFacadeService(verifierConfiguration, ariesFacadeClientMock);
		VerificationController verificationController =
				new VerificationController(verificationProcessService, verifiedClaimService,
						ariesFacadeService);

		Map<String, String> claims = verificationController.getVerifiedClaims(verificationProcess.getId()).getBody();
		assertThat(claims).hasSize(3);
	}

	@Test
	void testVerifiedClaimsAreDroppedOnReverification() {
		UUID connectionId = UUID.randomUUID();
		String exchangeId = UUID.randomUUID().toString();
		String exchangeId1 = UUID.randomUUID().toString();

		VerificationProcess verificationProcess = new VerificationProcess();
		verificationProcess.setStatus(VerificationProcessState.PRESENTATION_VERIFIED);
		verificationProcess.setConnectionId(connectionId.toString());
		verificationProcess.setPresentationExchangeId(exchangeId);
		verificationProcessRepository.save(verificationProcess);
		createVerifiedClaimRecordsWithState(verificationProcess);

		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.RESPONDED);

		com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse verificationResponseMock
				= new com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse();
		verificationResponseMock.setPresentationExchangeId(String.valueOf(exchangeId1));
		when(ariesFacadeClientMock.presentProofSendRequest(any(), any())).thenReturn(verificationResponseMock);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(verifierConfiguration, ariesFacadeClientMock);
		VerificationController verificationController = new VerificationController(verificationProcessService,
				verifiedClaimService, ariesFacadeService);
		Map<String, String> claims = verificationController.getVerifiedClaims(verificationProcess.getId()).getBody();
		assertThat(claims).hasSize(3);

		VerificationRequest verificationRequest = new VerificationRequest();
		verificationRequest.setConnectionId(connectionId);
		VerificationResponse verificationResponse = verificationController.verifyCredentials(verificationRequest).getBody();

		assert verificationResponse != null;
		claims = verificationController.getVerifiedClaims(verificationResponse.getProcessId()).getBody();
		assertThat(claims).isEmpty();
	}

	@Test
	void verifyCredentialsCreatesProcess() {
		UUID connectionId = UUID.randomUUID();

		var respondedMock = new com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse();
		respondedMock.setPresentationExchangeId(UUID.randomUUID().toString());

		var establishedMock = new com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse();
		establishedMock.setPresentationExchangeId(UUID.randomUUID().toString());

		when(ariesFacadeClientMock.presentProofSendRequest(any(), any()))
				.thenReturn(respondedMock)
				.thenReturn(establishedMock);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(verifierConfiguration, ariesFacadeClientMock);
		VerificationController verificationController =
				new VerificationController(verificationProcessService, verifiedClaimService,
						ariesFacadeService);

		VerificationRequest verificationRequest = new VerificationRequest();
		verificationRequest.setConnectionId(connectionId);

		// No Connection
		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.INIT);
		VerificationResponse resp = verificationController.verifyCredentials(verificationRequest).getBody();
		assertThat(resp.getStatus()).isEqualTo(VerStatus.NO_CONNECTION);
		assertThat(resp.getProcessId()).isNull();

		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.CREATED);
		resp = verificationController.verifyCredentials(verificationRequest).getBody();
		assertThat(resp.getStatus()).isEqualTo(VerStatus.NO_CONNECTION);
		assertThat(resp.getProcessId()).isNull();

		// Verification In Progress
		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.REQUESTED);
		resp = verificationController.verifyCredentials(verificationRequest).getBody();
		assertThat(resp.getStatus()).isEqualTo(VerStatus.NO_CONNECTION);
		assertThat(resp.getProcessId()).isNull();

		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.RESPONDED);
		resp = verificationController.verifyCredentials(verificationRequest).getBody();
		assertThat(resp.getStatus()).isEqualTo(VerStatus.IN_PROGRESS);
		assertThat(resp.getProcessId()).isNotNull();

		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.ESTABLISHED);
		resp = verificationController.verifyCredentials(verificationRequest).getBody();
		assertThat(resp.getStatus()).isEqualTo(VerStatus.IN_PROGRESS);
		assertThat(resp.getProcessId()).isNotNull();

		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.ABANDONED);
		resp = verificationController.verifyCredentials(verificationRequest).getBody();
		assertThat(resp.getStatus()).isEqualTo(VerStatus.NO_CONNECTION);
		assertThat(resp.getProcessId()).isNull();
	}


	@Test
	void processCanBeDeleted() {
		UUID connectionId = UUID.randomUUID();

		var respondedMock = new com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse();
		respondedMock.setPresentationExchangeId(UUID.randomUUID().toString());

		when(ariesFacadeClientMock.presentProofSendRequest(any(), any()))
				.thenReturn(respondedMock);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(verifierConfiguration, ariesFacadeClientMock);
		VerificationController verificationController =
				new VerificationController(verificationProcessService, verifiedClaimService,
						ariesFacadeService);

		VerificationRequest verificationRequest = new VerificationRequest();
		verificationRequest.setConnectionId(connectionId);

		when(ariesFacadeClientMock.getConnectionState(connectionId)).thenReturn(AriesConnectionState.RESPONDED);
		VerificationResponse resp = verificationController.verifyCredentials(verificationRequest).getBody();
		assertThat(resp.getStatus()).isEqualTo(VerStatus.IN_PROGRESS);
		assertThat(resp.getProcessId()).isNotNull();


		UUID processToDelete = resp.getProcessId();
		assertThat(verificationController.getVerificationState(processToDelete)).isEqualTo(ResponseEntity.ok(VerStatus.IN_PROGRESS));
		ResponseEntity<UUID> response = verificationController.deleteVerificationProcess(processToDelete);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isEqualTo(processToDelete);
		assertThatThrownBy(() -> verificationController.getVerificationState(processToDelete))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Cannot resolve the given {id} = " + processToDelete);

	}

	@Test
	void deletingNonExistingProcessIsOkay() {
		var respondedMock = new com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse();
		respondedMock.setPresentationExchangeId(UUID.randomUUID().toString());

		when(ariesFacadeClientMock.presentProofSendRequest(any(), any()))
				.thenReturn(respondedMock);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(verifierConfiguration, ariesFacadeClientMock);
		VerificationController verificationController =
				new VerificationController(verificationProcessService, verifiedClaimService,
						ariesFacadeService);

		UUID processToDelete = UUID.randomUUID();
		ResponseEntity<UUID> response = verificationController.deleteVerificationProcess(processToDelete);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(processToDelete);

	}
}
