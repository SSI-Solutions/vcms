package com.adnovum.vcms.verifier.service;

import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.CredentialDefinition;
import com.adnovum.vcms.verifier.VerifierServerIntBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AriesFacadeServiceTest extends VerifierServerIntBase {

	@Test
	void getVerifierCredentialDefinitionsFromLedger() {
		String test = "getVerifierCredentialDefinitionsFromLedger";

		AriesFacadeClient ariesFacadeClient = mock(AriesFacadeClient.class);

		CredentialDefinition credentialDefinition1 = new CredentialDefinition();
		credentialDefinition1.setCredentialDefinitionId(test + "_creDefId1");
		credentialDefinition1.setClaims(List.of(test + "_claim11", test + "_claim12"));
		CredentialDefinition credentialDefinition2 = new CredentialDefinition();
		credentialDefinition2.setCredentialDefinitionId(test + "_creDefId2");
		credentialDefinition2.setClaims(List.of(test + "_claim21", test + "_claim22"));
		when(ariesFacadeClient.getCredentialDefinitionsById(verifierConfiguration.getCreDefIds()))
				.thenReturn(List.of(credentialDefinition1, credentialDefinition2));

		CredentialDefinition credentialDefinition3 = new CredentialDefinition();
		credentialDefinition3.setCredentialDefinitionId(test + "_creDefId3");
		credentialDefinition3.setClaims(List.of(test + "_claim31", test + "_claim32"));
		when(ariesFacadeClient.getCredentialDefinitionsByCreation())
				.thenReturn(List.of(credentialDefinition1, credentialDefinition3));

		AriesFacadeService ariesFacadeService = new AriesFacadeService(verifierConfiguration, ariesFacadeClient);
		List<com.adnovum.vcms.genapi.verifier.server.dto.CredentialDefinition> credentialDefinitions
				= ariesFacadeService.getVerifierCredentialDefinitionsFromLedger();
		assertThat(credentialDefinitions).hasSize(3);
		List<String> creDefIds = credentialDefinitions.stream().map(x -> x.getCredentialDefinitionId()).collect(Collectors.toList());
		assertThat(creDefIds).contains(test + "_creDefId1", test + "_creDefId2", test + "_creDefId3");
	}
}
