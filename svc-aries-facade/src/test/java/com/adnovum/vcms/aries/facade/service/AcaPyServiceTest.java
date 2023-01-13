package com.adnovum.vcms.aries.facade.service;

import com.adnovum.vcms.aries.facade.AriesFacadeIntBase;
import com.adnovum.vcms.genapi.acapy.client.dto.ConnRecord;
import com.adnovum.vcms.genapi.acapy.client.dto.ConnectionInvitation;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionsCreatedResult;
import com.adnovum.vcms.genapi.acapy.client.dto.InvitationResult;
import com.adnovum.vcms.genapi.acapy.client.dto.Schema;
import com.adnovum.vcms.genapi.acapy.client.dto.SchemaGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.V10CredentialExchange;
import com.adnovum.vcms.genapi.acapy.client.dto.V10PresentationExchange;
import com.adnovum.vcms.genapi.acapy.client.dto.V10PresentationSendRequestRequest;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionInvitation;
import com.adnovum.vcms.genapi.aries.facade.server.dto.IssuingResponse;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationOption;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AcaPyServiceTest extends AriesFacadeIntBase {

	@Test
	void getConnectionInvitation() {
		String invitationUrl = "http://localhost:8044?c_i=eyJAdHlwZSI6ICJkaWQ6c292OkJ6Q2JzTlloTXJqSGlxWkRUVUFTSGc7c3BlYy9jb25uZWN" +
				"0aW9ucy8xLjAvaW52aXRhdGlvbiIsICJAaWQiOiAiNzY2YzExYzgtMzEwYS00NjBlLWIyM2EtYzdjYTIyYjkwNzIyIiwgImltYWdlVXJsIjogImh" +
				"0dHBzOi8vd3d3LmRpdmVzc2kuY29tL3R5cG8zY29uZi9leHQvc3NpX2NvcnBvcmF0ZV9jb3JlL1Jlc291cmNlcy9QdWJsaWMvSW1hZ2VzL3NzaS1" +
				"sb2dvLnBuZyIsICJyZWNpcGllbnRLZXlzIjogWyJ1MzVmRE1Od2RSVFI4M3NxakJ3ZU5IY25wY2ZwS3paN210dWhSTWJTckE5Il0sICJsYWJlbCI" +
				"6ICJTU0ktT0lEQy1CcmlkZ2UiLCAic2VydmljZUVuZHBvaW50IjogImh0dHA6Ly9mZDI3LTIxNy0xNjItMS02Lm5ncm9rLmlvIn0=";
		String connectionId = UUID.randomUUID().toString();
		String recipientKey  = "u35fDMNwdRT83sqjBweNHcnpcfpKzZ7mtuhRMbSrA9";

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		InvitationResult invitationResult = new InvitationResult();
		invitationResult.setInvitationUrl(invitationUrl);
		invitationResult.setConnectionId(connectionId);

		ConnectionInvitation connectionInvitation = new ConnectionInvitation();
		connectionInvitation.setRecipientKeys(List.of(recipientKey));
		invitationResult.setInvitation(connectionInvitation);

		when(acaPyClientMock.createInvitation(any())).thenReturn(invitationResult);

		AriesService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);
		AriesConnectionInvitation invitation = acaPyService.getConnectionInvitation();

		assertThat(invitation.getConnectionId()).isEqualTo(UUID.fromString(connectionId));
		assertThat(invitation.getInvitationUrl()).isEqualTo(invitationUrl);
	}

	@Test
	void sendAuthPresentProofRequest() {
		String test = "sendAuthPresentProofRequest";
		UUID connectionId = UUID.randomUUID();

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		when(acaPyClientMock.presentProofSendRequest(any())).thenReturn(new V10PresentationExchange());
		ConnRecord connRecord = new ConnRecord();
		connRecord.setConnectionId(String.valueOf(connectionId));
		connRecord.setState("completed");
		when(acaPyClientMock.getConnectionById(String.valueOf(connectionId))).thenReturn(connRecord);
		AriesService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);

		VerificationOption verificationOption = new VerificationOption();
		verificationOption.setCredentialDefinitionId(test);
		verificationOption.setAttributes(List.of(test));
		VerificationResponse verificationResponse = acaPyService.sendProofRequest(String.valueOf(connectionId), verificationOption);

		assertThat(verificationResponse).isNotNull();
	}

	@Test
	void generateProofRequest() {
		String test = "generateProofRequest";
		UUID randomUuid = UUID.randomUUID();

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		AcaPyService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);

		VerificationOption verificationOption = new VerificationOption();
		verificationOption.setCredentialDefinitionId(test);
		verificationOption.setAttributes(List.of(test + "_attribute1", test + "_attribute2"));

		V10PresentationSendRequestRequest v10PresentationSendRequestRequest = acaPyService.generateProofRequest(
				String.valueOf(randomUuid), verificationOption, acaPyProperties.getComment(), acaPyProperties.getProofName(),
				acaPyProperties.getNonce());

		assertThat(v10PresentationSendRequestRequest.getConnectionId()).isEqualTo(randomUuid);
		assertThat(v10PresentationSendRequestRequest.getProofRequest().getRequestedAttributes()).hasSize(2);
		assertThat(v10PresentationSendRequestRequest.getProofRequest().getRequestedAttributes().get(test + "_attribute1")
				.getRestrictions().get(0)).containsEntry("cred_def_id", test);
	}

	@Test
	void sendCredentialOffer() {
		String test = "generateProofRequest";
		UUID randomUuid = UUID.randomUUID();

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		Map<String, String> claimMap = new HashMap<>();
		when(acaPyClientMock.sendCredentialOffer(test, randomUuid, acaPyProperties.getComment(), claimMap))
				.thenReturn(new V10CredentialExchange());
		AcaPyService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);
		IssuingResponse issuingResponse = acaPyService.sendCredentialOffer(test, randomUuid, acaPyProperties.getComment(), claimMap);
		assertThat(issuingResponse).isNotNull();
	}

	@Test
	void issueCredentialRecord() {
		String test = "issueCredentialRecord";

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		when(acaPyClientMock.issueCredentialRecord(test, acaPyProperties.getComment())).thenReturn(new V10CredentialExchange());

		AcaPyService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);
		IssuingResponse issuingResponse = acaPyService.issueCredentialRecord(test, acaPyProperties.getComment());
		assertThat(issuingResponse).isNotNull();
	}

	@Test
	void getCredentialDefinitionsFromLedger() {
		String test = "getCredentialDefinitionsFromLedger";

		CredentialDefinition credentialDefinition = new CredentialDefinition();
		credentialDefinition.setId(test + "_creDefId");
		credentialDefinition.setSchemaId(test + "_schemaId");
		CredentialDefinitionGetResult credentialDefinitionGetResults = new CredentialDefinitionGetResult();
		credentialDefinitionGetResults.setCredentialDefinition(credentialDefinition);

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		when(acaPyClientMock.getCredentialDefinitionById(test + "_creDefId")).thenReturn(credentialDefinitionGetResults);

		SchemaGetResult schemaGetResults = new SchemaGetResult();
		Schema schema = new Schema();
		schema.setAttrNames(List.of(test + "_attribute1", test + "_attribute2"));
		schemaGetResults.setSchema(schema);
		when(acaPyClientMock.getSchemaById(test + "_schemaId")).thenReturn(schemaGetResults);

		AcaPyService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);
		List<com.adnovum.vcms.genapi.aries.facade.server.dto.CredentialDefinition> credentialDefinitions
				= acaPyService.getCredentialDefinitionsFromLedger(List.of(test + "_creDefId"));
		assertThat(credentialDefinitions).hasSize(1);
		assertThat(credentialDefinitions.get(0).getCredentialDefinitionId()).isEqualTo(test + "_creDefId");
		assertThat(credentialDefinitions.get(0).getClaims()).hasSize(2);
	}

	@Test
	void getCreatedCredentialDefinitions() {
		String test = "getCreatedCredentialDefinitions";

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		CredentialDefinitionsCreatedResult credentialDefinitionsCreatedResults = new CredentialDefinitionsCreatedResult();
		credentialDefinitionsCreatedResults.setCredentialDefinitionIds(List.of(test + "_creDefId1", test + "_creDefId2"));
		when(acaPyClientMock.getCreatedCredentialDefinitions()).thenReturn(credentialDefinitionsCreatedResults);

		CredentialDefinitionGetResult credentialDefinitionGetResults1 = new CredentialDefinitionGetResult();
		CredentialDefinition credentialDefinition1 = new CredentialDefinition();
		credentialDefinition1.setSchemaId(test + "_schema1");
		credentialDefinitionGetResults1.setCredentialDefinition(credentialDefinition1);
		when(acaPyClientMock.getCredentialDefinitionById(test + "_creDefId1")).thenReturn(credentialDefinitionGetResults1);

		CredentialDefinitionGetResult credentialDefinitionGetResults2 = new CredentialDefinitionGetResult();
		CredentialDefinition credentialDefinition2 = new CredentialDefinition();
		credentialDefinition2.setSchemaId(test + "_schema2");
		credentialDefinitionGetResults2.setCredentialDefinition(credentialDefinition2);
		when(acaPyClientMock.getCredentialDefinitionById(test + "_creDefId2")).thenReturn(credentialDefinitionGetResults2);

		SchemaGetResult schemaGetResults1 = new SchemaGetResult();
		Schema schema1 = new Schema();
		schema1.setId(test + "_schema1");
		schema1.setAttrNames(List.of(test + "_attribute11", test + "_attribute12"));
		schemaGetResults1.setSchema(schema1);
		when(acaPyClientMock.getSchemaById(test + "_schema1")).thenReturn(schemaGetResults1);

		SchemaGetResult schemaGetResults2 = new SchemaGetResult();
		Schema schema2 = new Schema();
		schema2.setId(test + "_schema2");
		schema2.setAttrNames(List.of(test + "_attribute21", test + "_attribute22"));
		schemaGetResults2.setSchema(schema2);
		when(acaPyClientMock.getSchemaById(test + "_schema2")).thenReturn(schemaGetResults2);

		AcaPyService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);
		List<com.adnovum.vcms.genapi.aries.facade.server.dto.CredentialDefinition> credentialDefinitions
				= acaPyService.getCreatedCredentialDefinitions();
		assertThat(credentialDefinitions).hasSize(2);
	}

	@Test
	void isCreDefIdCreatedByThisWallet() {
		String test = "isCreDefIdCreatedByThisWallet";

		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		CredentialDefinitionsCreatedResult credentialDefinitionsCreatedResults = new CredentialDefinitionsCreatedResult();
		credentialDefinitionsCreatedResults.setCredentialDefinitionIds(List.of(test + "_creDefId1", test + "_creDefId2"));
		when(acaPyClientMock.getCreatedCredentialDefinitions()).thenReturn(credentialDefinitionsCreatedResults);

		AcaPyService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);
		assertThat(acaPyService.isCreDefIdCreatedByThisWallet(test + "_creDefId1")).isTrue();
		assertThat(acaPyService.isCreDefIdCreatedByThisWallet(test + "_creDefId3")).isFalse();
	}
}
