package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.client.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.aries.facade.client.dto.IssuingOption;
import com.adnovum.vcms.genapi.aries.facade.client.dto.IssuingResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AriesFacadeServiceTest {

	@Test
	void getCreatedCredentialDefinitions() {
		String test = "getCreatedCredentialDefinitions";
		AriesFacadeClient ariesFacadeClientMock = mock(AriesFacadeClient.class);

		CredentialDefinition credentialDefinition1 = new CredentialDefinition();
		credentialDefinition1.setCredentialDefinitionId(test);
		credentialDefinition1.setClaims(List.of(test + "_claim11", test + "_claim12"));
		CredentialDefinition credentialDefinition2 = new CredentialDefinition();
		credentialDefinition2.setCredentialDefinitionId(test);
		credentialDefinition2.setClaims(List.of(test + "_claim21", test + "_claim22"));

		when(ariesFacadeClientMock.getCredentialDefinitionsByCreation()).thenReturn(List.of(credentialDefinition1, credentialDefinition2));
		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClientMock);
		List<com.adnovum.vcms.genapi.issuer.server.dto.CredentialDefinition> credentialDefinitionList = ariesFacadeService.getCreatedCredentialDefinitions();
		assertThat(credentialDefinitionList).hasSize(2);
	}

	@Test
	void isCreatedCreDef() {
		UUID randomUuid = UUID.randomUUID();

		AriesFacadeClient ariesFacadeClientMock = mock(AriesFacadeClient.class);
		when(ariesFacadeClientMock.isCreatedCreDef(String.valueOf(randomUuid))).thenReturn(Boolean.TRUE);
		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClientMock);

		assertThat(ariesFacadeService.isCreatedCreDef(String.valueOf(randomUuid))).isTrue();
	}

	@Test
	void offerCredential() {
		String test = "offerCredential";
		UUID connectionId = UUID.randomUUID();
		String credentialExchangeId = UUID.randomUUID().toString();

		AriesFacadeClient ariesFacadeClientMock = mock(AriesFacadeClient.class);

		IssuingOption issuingOption = new IssuingOption();
		String creDefId = test + "_creDefId";
		issuingOption.setCredentialDefinitionId(test + "_creDefId");
		Map<String, String> claims = Map.of(test + "_key1",
				test + "_value1", test + "_key2", test + "_value2", test + "_key3", test + "_value3");
		issuingOption.setAttributes(claims);
		IssuingResponse issuingResponse = new IssuingResponse();
		issuingResponse.setConnectionId(String.valueOf(connectionId));
		issuingResponse.setCredentialExchangeId(credentialExchangeId);
		issuingResponse.setCredentialDefinitionId(creDefId);
		when(ariesFacadeClientMock.offerCredential(connectionId, issuingOption)).thenReturn(issuingResponse);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClientMock);
		assertThat(ariesFacadeService.offerCredential(connectionId, creDefId, claims)).isEqualTo(credentialExchangeId);
	}

	@Test
	void issueCredential() {
		UUID randomUuid = UUID.randomUUID();

		AriesFacadeClient ariesFacadeClientMock = mock(AriesFacadeClient.class);
		IssuingResponse issuingResponse = new IssuingResponse();
		issuingResponse.setConnectionId(String.valueOf(randomUuid));
		when(ariesFacadeClientMock.issueCredential(randomUuid)).thenReturn(issuingResponse);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClientMock);
		issuingResponse = ariesFacadeService.issueCredential(randomUuid);
		assertThat(issuingResponse.getConnectionId()).isEqualTo(String.valueOf(randomUuid));
	}

	@Test
	void getConnectionStateById() {
		UUID randomUuid = UUID.randomUUID();

		AriesFacadeClient ariesFacadeClientMock = mock(AriesFacadeClient.class);
		when(ariesFacadeClientMock.getConnectionState(randomUuid)).thenReturn(AriesConnectionState.CREATED);

		AriesFacadeService ariesFacadeService = new AriesFacadeService(ariesFacadeClientMock);
		assertThat(ariesFacadeService.getConnectionStateById(randomUuid)).isEqualTo(AriesConnectionState.CREATED);
	}
}
