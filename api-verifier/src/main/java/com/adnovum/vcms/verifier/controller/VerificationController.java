package com.adnovum.vcms.verifier.controller;

import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.verifier.server.controller.VerifyApi;
import com.adnovum.vcms.genapi.verifier.server.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.verifier.server.dto.VerStatus;
import com.adnovum.vcms.genapi.verifier.server.dto.VerificationRequest;
import com.adnovum.vcms.genapi.verifier.server.dto.VerificationResponse;
import com.adnovum.vcms.verifier.datamodel.entity.VerificationProcess;
import com.adnovum.vcms.verifier.datamodel.entity.VerifiedClaim;
import com.adnovum.vcms.verifier.service.AriesFacadeService;
import com.adnovum.vcms.verifier.service.VerificationProcessService;
import com.adnovum.vcms.verifier.service.VerifiedClaimService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState.ESTABLISHED;
import static com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState.RESPONDED;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "Verifier", description = "Manage verification processes to reveal claims of a VC")
public class VerificationController implements VerifyApi {

	private final VerificationProcessService verificationProcessService;

	private final VerifiedClaimService verifiedClaimService;

	private final AriesFacadeService ariesFacadeService;

	@Override
	public ResponseEntity<List<CredentialDefinition>> getCredentialDefinitions() {
		return ResponseEntity.ok(ariesFacadeService.getVerifierCredentialDefinitionsFromLedger());
	}

	@Override
	public ResponseEntity<VerStatus> getVerificationState(UUID processId) {
		return ResponseEntity.ok(verificationProcessService.getVerificationProcessById(processId).getStatus().getVerStatus());
	}

	@Override
	public ResponseEntity<Map<String, String>> getVerifiedClaims(UUID processId) {
		VerificationProcess verificationProcess = verificationProcessService.getVerificationProcessById(processId);
		List<VerifiedClaim> verifiedClaims = verifiedClaimService.getVerifiedClaimsByVerificationProcess(verificationProcess);
		Map<String, String> claimsMap =
				verifiedClaims.stream().collect(Collectors.toMap(VerifiedClaim::getClaimKey, VerifiedClaim::getClaimValue));
		return ResponseEntity.ok(claimsMap);
	}

	@Override
	public ResponseEntity<VerificationResponse> verifyCredentials(VerificationRequest verificationRequest) {
		UUID connectionId = verificationRequest.getConnectionId();
		log.info("verification request received - sending proof request over connection {}", connectionId);

		VerificationResponse verificationResponse = new VerificationResponse();
		AriesConnectionState connectionState = ariesFacadeService.getConnectionById(connectionId);
		if (!(RESPONDED.equals(connectionState) || ESTABLISHED.equals(connectionState))) {
			verificationResponse.setStatus(VerStatus.NO_CONNECTION);
			return ResponseEntity.ok(verificationResponse);
		}

		String presentationExchangeId = ariesFacadeService.presentProofSendRequest(connectionId,
				verificationRequest.getCredentialDefinitionId(), verificationRequest.getAttributes());

		log.info("creating verification state for connection {} and exchange {}", connectionId, presentationExchangeId);
		VerificationProcess verificationProcess = verificationProcessService.createVerificationProcess(connectionId.toString(),
				presentationExchangeId);

		verificationResponse.setStatus(verificationProcess.getStatus().getVerStatus());
		verificationResponse.setProcessId(verificationProcess.getId());
		return ResponseEntity.ok(verificationResponse);
	}

	@Override
	public ResponseEntity<UUID> deleteVerificationProcess(UUID processId) {
		log.info("Process deletion triggered for {}", processId);
		if (Boolean.TRUE.equals(verificationProcessService.doesProcessExist(processId))) {
			VerificationProcess verificationProcess = verificationProcessService.getVerificationProcessById(processId);
			verificationProcessService.deleteByProcess(verificationProcess);
			return ResponseEntity.status(204).body(processId);

		}
		return ResponseEntity.ok(processId);
	}
}
