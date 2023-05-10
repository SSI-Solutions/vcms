package com.adnovum.vcms.aries.facade.service;


import com.adnovum.vcms.aries.facade.AriesFacadeIntBase;
import com.adnovum.vcms.genapi.acapy.client.dto.ConnRecord;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionsCreatedResult;
import com.adnovum.vcms.genapi.acapy.client.dto.InvitationResult;
import com.adnovum.vcms.genapi.acapy.client.dto.SchemaGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.V10CredentialExchange;
import com.adnovum.vcms.genapi.acapy.client.dto.V10PresentationExchange;
import com.adnovum.vcms.genapi.acapy.client.dto.V10PresentationSendRequestRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AcaPyClientTest extends AriesFacadeIntBase {

	public static MockWebServer mockBackEnd;

	@Autowired
	private ObjectMapper objectMapper;

	private AcaPyClient acapyClient;

	@AfterEach
	void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@BeforeEach
	void initialize() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
		String baseUrl = String.format(acaPyProperties.getBasePath() + ":%s", mockBackEnd.getPort());
		AcaPyProperties acapyProperties = new AcaPyProperties();
		acapyProperties.setBasePath(baseUrl);
		acapyProperties.setCredentialOfferAutoIssue(acaPyProperties.getCredentialOfferAutoIssue());
		acapyProperties.setCredentialOfferTrace(acaPyProperties.getCredentialOfferTrace());
		acapyProperties.setCredentialOfferAutoRemove(acaPyProperties.getCredentialOfferAutoIssue());
		acapyClient = new AcaPyClient(acapyProperties);
	}

	@Test
	void createInvitation() throws JsonProcessingException, InterruptedException {
		String alias = "conn_alias";
		InvitationResult mockResponse = new InvitationResult();
		mockResponse.setInvitationUrl(invitationURL);

		mockResponse.setConnectionId(testUUID.toString());
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(mockResponse))
				.addHeader("Content-Type", "application/json"));

		assertThat(acapyClient.createInvitation(alias)).isEqualTo(mockResponse);
		RecordedRequest request = mockBackEnd.takeRequest();
		assertThat(request.getBody().readUtf8()).isEmpty();
		assertThat(request.getRequestLine()).isEqualTo(
				"POST /connections/create-invitation?alias=" + alias + "&auto_accept=true&multi_use=false&public=false HTTP/1"
						+ ".1");
	}

	@Test
	void getCredentialDefinitionById() throws JsonProcessingException, InterruptedException {
		CredentialDefinitionGetResult credentialDefinitionGetResults = new CredentialDefinitionGetResult();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(credentialDefinitionGetResults))
				.addHeader("Content-Type", "application/json"));
		assertThat(acapyClient.getCredentialDefinitionById(credDefID)).isEqualTo(credentialDefinitionGetResults);

		RecordedRequest request = mockBackEnd.takeRequest();
		assertThat(request.getBody().readUtf8()).isEmpty();
		assertThat(request.getRequestLine()).isEqualTo("GET /credential-definitions/" + credDefID + " HTTP/1.1");

	}

	@Test
	void getCreatedCredentialDefinitions() throws JsonProcessingException, InterruptedException {
		CredentialDefinitionsCreatedResult credentialDefinitionsCreatedResults = new CredentialDefinitionsCreatedResult();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(credentialDefinitionsCreatedResults))
				.addHeader("Content-Type", "application/json"));
		assertThat(acapyClient.getCreatedCredentialDefinitions()).isEqualTo(credentialDefinitionsCreatedResults);

		RecordedRequest request = mockBackEnd.takeRequest();
		assertThat(request.getBody().readUtf8()).isEmpty();
		assertThat(request.getRequestLine()).isEqualTo("GET /credential-definitions/created HTTP/1.1");
	}

	@Test
	void getSchemaById() throws JsonProcessingException, InterruptedException {
		SchemaGetResult schemaGetResults = new SchemaGetResult();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(schemaGetResults))
				.addHeader("Content-Type", "application/json"));
		assertThat(acapyClient.getSchemaById(schemaID)).isEqualTo(schemaGetResults);

		RecordedRequest request = mockBackEnd.takeRequest();
		assertThat(request.getBody().readUtf8()).isEmpty();
		assertThat(request.getRequestLine()).isEqualTo("GET /schemas/" + schemaID + " HTTP/1.1");
	}

	@Test
	void presentProofSendRequest() throws JsonProcessingException, InterruptedException {
		String test = "generateProofRequest";
		UUID randomUuid = UUID.randomUUID();

		V10PresentationExchange mockResponse = new V10PresentationExchange();
		AcaPyClient acaPyClientMock = mock(AcaPyClient.class);
		AcaPyService acaPyService = new AcaPyService(acaPyClientMock, acaPyProperties);

		com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationOption
				verificationOption = new com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationOption();
		verificationOption.setCredentialDefinitionId(test);
		verificationOption.setAttributes(List.of(test + "_attribute1", test + "_attribute2"));

		V10PresentationSendRequestRequest v10PresentationSendRequestRequest = acaPyService.generateProofRequest(
				String.valueOf(randomUuid), verificationOption, acaPyProperties.getComment(), acaPyProperties.getProofName(),
				acaPyProperties.getNonce());


		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(mockResponse))
				.addHeader("Content-Type", "application/json"));
		assertThat(acapyClient.presentProofSendRequest(v10PresentationSendRequestRequest)).isEqualTo(mockResponse);

		RecordedRequest request = mockBackEnd.takeRequest();
		String requestBody = request.getBody().readUtf8();

		// Non-revocation timestamps are removed from matching
		assertThat(requestBody)
				.containsSubsequence("{\"comment\":\"present proof request\",\"connection_id\":\"",
						randomUuid.toString(),
						"\",\"proof_request",
						"\":{\"name\":\"present proof request\",\"non_revoked\":{\"from\":",
						"\"to\":",
						"},\"nonce\":\"78789\",",
						"\"requested_attributes\":{\"generateProofRequest_attribute2\":{\"name"
						, "\":\"generateProofRequest_attribute2\","
						, "\"non_revoked\":{\"from\":",
						"\"to\":",
						"},\"restrictions\":[{\"cred_def_id\":\"generateProofRequest\"}]},"
						, "\"generateProofRequest_attribute1\":{\"name\":\"generateProofRequest_attribute1\","
						, "\"non_revoked\":{\"from\":",
						"\"to\":",
						"},\"restrictions\":[{\"cred_def_id\":\"generateProofRequest\"}]}},\"requested_predicates\":{},"
						, "\"version\":\"1"
						, ".0\"},\"trace\":false}");
		assertThat(requestBody).doesNotContain("null");
		assertThat(request.getRequestLine()).isEqualTo("POST /present-proof/send-request HTTP/1.1");
	}

	@Test
	void getConnectionById() throws JsonProcessingException, InterruptedException {
		ConnRecord connRecord = new ConnRecord();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(connRecord))
				.addHeader("Content-Type", "application/json"));

		assertThat(acapyClient.getConnectionById(testUUID.toString())).isEqualTo(connRecord);

		RecordedRequest request = mockBackEnd.takeRequest();
		assertThat(request.getBody().readUtf8()).isEmpty();
		assertThat(request.getRequestLine()).isEqualTo("GET /connections/" + testUUID + " HTTP/1.1");
	}

	@Test
	void sendCredentialOfferCreatesValidRequestWithMultipleTextClaims() throws JsonProcessingException, InterruptedException {
		String test = "sendCredentialOffer";
		UUID connectionId = UUID.randomUUID();
		V10CredentialExchange mockresponse = new V10CredentialExchange();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(mockresponse))
				.addHeader("Content-Type", "application/json"));

		Map<String, String> claimMap = new HashMap<>();
		claimMap.put(test + "_claim1", test + "_value1");
		claimMap.put(test + "_claim2", test + "_value2");
		claimMap.put(test + "_claim3", test + "_value3");

		assertThat(acapyClient.sendCredentialOffer(test + "_credfDefId", connectionId, test + "_comment",
				claimMap)).isEqualTo(mockresponse);

		RecordedRequest request = mockBackEnd.takeRequest();

		assertThat(request.getBody().readUtf8()).isEqualTo(
				"{\"auto_issue\":false,\"auto_remove\":false,\"comment\":\"sendCredentialOffer_comment\","
						+ "\"connection_id\":\"" + connectionId + "\","
						+ "\"cred_def_id\":\"sendCredentialOffer_credfDefId\","
						+ "\"credential_preview\":{\"@type\":\"https://didcomm"
						+ ".org/issue-credential/2.0/credential-preview\",\"attributes\":[{\"mime-type\":\"text/plain\","
						+ "\"name\":\"sendCredentialOffer_claim3\",\"value\":\"sendCredentialOffer_value3\"},"
						+ "{\"mime-type\":\"text/plain\","
						+ "\"name\":\"sendCredentialOffer_claim2\",\"value\":\"sendCredentialOffer_value2\"},"
						+ "{\"mime-type\":\"text/plain\","
						+ "\"name\":\"sendCredentialOffer_claim1\",\"value\":\"sendCredentialOffer_value1\"}]},"
						+ "\"trace\":true}");
		assertThat(request.getRequestLine()).isEqualTo("POST /issue-credential/send-offer HTTP/1.1");
	}

	@ParameterizedTest
	@CsvSource(value = {
			"simple text Claim.text/plain.simple text Claim",
			"data:,implicitTextClaim.text/plain.data:,implicitTextClaim",
			"data:text/plain;base64,fullDataUriIssued.text/plain.data:text/plain;base64,fullDataUriIssued",
			"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/4QBMRXhpZg.image/jpeg.data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/4QBMRXhpZg",
	        "data:image/png;base64,aW1hZ2VEYXRhSlBHAAAA.image/png.data:image/png;base64,aW1hZ2VEYXRhSlBHAAAA"}, delimiter = '.')
	void sendCredentialOfferWhereSomeClaimsAreDataURIs(String dataFromFrontend, String expectedMimeType, String expectedClaimValue) throws JsonProcessingException, InterruptedException {
		String test = "datauri";
		UUID connectionId = UUID.randomUUID();
		V10CredentialExchange mockresponse = new V10CredentialExchange();
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(mockresponse))
				.addHeader("Content-Type", "application/json"));

		Map<String, String> claimMap = new HashMap<>();
		claimMap.put("testClaimKey", dataFromFrontend);


		assertThat(acapyClient.sendCredentialOffer(test + "_credfDefId", connectionId, test + "_comment",
				claimMap)).isEqualTo(mockresponse);

		RecordedRequest request = mockBackEnd.takeRequest();

		assertThat(request.getBody().readUtf8()).isEqualTo(
				"{\"auto_issue\":false,\"auto_remove\":false,\"comment\":\"datauri_comment\","
						+ "\"connection_id\":\"" + connectionId + "\","
						+ "\"cred_def_id\":\"datauri_credfDefId\","
						+ "\"credential_preview\":{\"@type\":\"https://didcomm"
						+ ".org/issue-credential/2.0/credential-preview\",\"attributes\":[{\"mime-type\":\""+expectedMimeType+"\","
						+ "\"name\":\"testClaimKey\",\"value\":\""+expectedClaimValue+"\"}]},"
						+ "\"trace\":true}");
		assertThat(request.getRequestLine()).isEqualTo("POST /issue-credential/send-offer HTTP/1.1");
	}


	@Test
	void issueCredentialRecord() throws JsonProcessingException, InterruptedException {
		String test = "issueCredentialRecord";
		String credentialID = "CRED ID";
		String credExId = test + "_credExId";
		String testComment = test + "_comment";
		V10CredentialExchange v10CredentialExchange = new V10CredentialExchange();
		v10CredentialExchange.setCredentialId(credentialID);
		mockBackEnd.enqueue(new MockResponse()
				.setBody(objectMapper.writeValueAsString(v10CredentialExchange))
				.addHeader("Content-Type", "application/json"));
		assertThat(acapyClient.issueCredentialRecord(credExId, testComment)).isEqualTo(v10CredentialExchange);

		RecordedRequest request = mockBackEnd.takeRequest();
		assertThat(request.getBody().readUtf8()).isEqualTo("{\"comment\":\"" + testComment + "\"}");
		assertThat(request.getRequestLine()).isEqualTo("POST /issue-credential/records/" + credExId + "/issue HTTP/1.1");
	}

	@Test
	void revokeCredential() throws InterruptedException {
		UUID testID = UUID.randomUUID();

		mockBackEnd.enqueue(new MockResponse()
				.setBody("{\"value\":\"test\"}")
				.addHeader("Content-Type", "application/json"));

		// Response is an empty object, nothing to assert
		acapyClient.revokeCredential(testID.toString());

		RecordedRequest request = mockBackEnd.takeRequest();
		String requestBody = request.getBody().readUtf8();
		assertThat(requestBody).isEqualTo("{\"cred_ex_id\":\"" + testID + "\",\"publish\":true}");
	}
}
