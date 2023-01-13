package com.adnovum.vcms.aries.facade.controller;

import com.adnovum.vcms.aries.facade.service.AriesService;
import com.adnovum.vcms.genapi.aries.facade.server.controller.CredentialsApi;
import com.adnovum.vcms.genapi.aries.facade.server.dto.CredentialDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CredentialController implements CredentialsApi {

	private final AriesService ariesService;

	@Override
	public ResponseEntity<List<CredentialDefinition>> getCreatedCredentialDefinitions() {
		return ResponseEntity.ok(ariesService.getCreatedCredentialDefinitions());
	}

	@Override
	public ResponseEntity<List<CredentialDefinition>> getCredentialDefinitionsById(List<String> creDefIds) {
		return ResponseEntity.ok(ariesService.getCredentialDefinitionsFromLedger(creDefIds));
	}

	@Override
	public ResponseEntity<Boolean> isCreatedCredentialDefinition(String creDefId) {
		return ResponseEntity.ok(ariesService.isCreDefIdCreatedByThisWallet(creDefId));
	}

	@Override
	public ResponseEntity<Void> revokeCredential(String credExchId) {
		ariesService.revokeCredential(credExchId);
		return ResponseEntity.ok().build();
	}
}
