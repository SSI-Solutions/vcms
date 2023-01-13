package com.adnovum.vcms.aries.facade.controller;

import com.adnovum.vcms.aries.facade.service.AriesService;
import com.adnovum.vcms.genapi.aries.facade.server.controller.ConnectionsApi;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionInvitation;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.server.dto.IssuingOption;
import com.adnovum.vcms.genapi.aries.facade.server.dto.IssuingResponse;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationOption;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ConnectionController implements ConnectionsApi {

	private final AriesService ariesService;

	@Override
	public ResponseEntity<AriesConnectionInvitation> connectionInvitation() {
		return ResponseEntity.ok(ariesService.getConnectionInvitation());
	}

	@Override
	public ResponseEntity<AriesConnectionState> connectionState(UUID connectionId) {
		return ResponseEntity.ok(ariesService.getConnectionById(String.valueOf(connectionId)));
	}

	@Override
	public ResponseEntity<IssuingResponse> offerCredential(UUID connectionId, IssuingOption issuingOption) {
		return ResponseEntity.ok(ariesService.sendCredentialOffer(issuingOption.getCredentialDefinitionId(), connectionId,
				"comment", issuingOption.getAttributes()));
	}

	@Override
	public ResponseEntity<VerificationResponse> verifyCredentials(UUID connectionId, VerificationOption verificationOption) {
		return ResponseEntity.ok(ariesService.sendProofRequest(String.valueOf(connectionId), verificationOption));
	}
}
