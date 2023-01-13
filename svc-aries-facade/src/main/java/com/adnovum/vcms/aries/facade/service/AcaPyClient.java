package com.adnovum.vcms.aries.facade.service;


import com.adnovum.vcms.genapi.acapy.client.ApiClient;
import com.adnovum.vcms.genapi.acapy.client.controller.ConnectionApi;
import com.adnovum.vcms.genapi.acapy.client.controller.CredentialDefinitionApi;
import com.adnovum.vcms.genapi.acapy.client.controller.IssueCredentialV10Api;
import com.adnovum.vcms.genapi.acapy.client.controller.PresentProofV10Api;
import com.adnovum.vcms.genapi.acapy.client.controller.RevocationApi;
import com.adnovum.vcms.genapi.acapy.client.controller.SchemaApi;
import com.adnovum.vcms.genapi.acapy.client.dto.ConnRecord;
import com.adnovum.vcms.genapi.acapy.client.dto.CredAttrSpec;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionsCreatedResult;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialPreview;
import com.adnovum.vcms.genapi.acapy.client.dto.InvitationResult;
import com.adnovum.vcms.genapi.acapy.client.dto.RevokeRequest;
import com.adnovum.vcms.genapi.acapy.client.dto.SchemaGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.V10CredentialExchange;
import com.adnovum.vcms.genapi.acapy.client.dto.V10CredentialFreeOfferRequest;
import com.adnovum.vcms.genapi.acapy.client.dto.V10CredentialIssueRequest;
import com.adnovum.vcms.genapi.acapy.client.dto.V10PresentationExchange;
import com.adnovum.vcms.genapi.acapy.client.dto.V10PresentationSendRequestRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AcaPyClient {

	private final ConnectionApi connectionApi;

	private final PresentProofV10Api presentProofV10Api;

	private final CredentialDefinitionApi credentialDefinitionApi;

	private final IssueCredentialV10Api issueCredentialV10Api;

	private final RevocationApi revocationApi;

	private final SchemaApi schemaApi;

	private final MappingJackson2HttpMessageConverter springMvcJacksonConverter;

	@Autowired
	public AcaPyClient(AcaPyProperties acapyProperties) {
		springMvcJacksonConverter = createMappingJacksonHttpMessageConverter();
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(springMvcJacksonConverter);
		restTemplate.setMessageConverters(converters);
		ApiClient apiClient = new ApiClient(restTemplate);
		apiClient.setBasePath(acapyProperties.getBasePath());

		connectionApi = new ConnectionApi(apiClient);
		presentProofV10Api = new PresentProofV10Api(apiClient);
		credentialDefinitionApi = new CredentialDefinitionApi(apiClient);
		issueCredentialV10Api = new IssueCredentialV10Api(apiClient);
		revocationApi = new RevocationApi(apiClient);
		schemaApi = new SchemaApi(apiClient);
	}

	protected ObjectMapper createObjectMapper() {
		return new ObjectMapper()
				.registerModule(new JsonNullableModule())
				.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	protected MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(createObjectMapper());
		return converter;
	}

	protected InvitationResult createInvitation(String alias) {
		return connectionApi.connectionsCreateInvitationPost(alias, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null);
	}

	protected ConnRecord getConnectionById(String connectionId) {
		return connectionApi.connectionsConnIdGet(connectionId);
	}

	protected CredentialDefinitionGetResult getCredentialDefinitionById(String creDefId) {
		return credentialDefinitionApi.credentialDefinitionsCredDefIdGet(creDefId);
	}

	protected CredentialDefinitionsCreatedResult getCreatedCredentialDefinitions() {
		return credentialDefinitionApi.credentialDefinitionsCreatedGet(null, null, null,
				null, null, null);
	}

	protected SchemaGetResult getSchemaById(String schemaId) {
		return schemaApi.schemasSchemaIdGet(schemaId);
	}

	protected V10PresentationExchange presentProofSendRequest(V10PresentationSendRequestRequest request) {
		return presentProofV10Api.presentProofSendRequestPost(request);
	}

	protected V10CredentialExchange sendCredentialOffer(String creDefId, UUID connectionId, String comment, Map<String, String> claimMap) {

		List<CredAttrSpec> attributes = new LinkedList<>();
		for (Map.Entry<String, String> entry: claimMap.entrySet()) {
			CredAttrSpec attribute = new CredAttrSpec();
			attribute.setValue(entry.getValue() == null ? "" : entry.getValue());
			attribute.setMimeType("text/plain");
			attribute.setName(entry.getKey());
			attributes.add(attribute);
		}

		CredentialPreview credentialPreview = new CredentialPreview();
		credentialPreview.setAttributes(attributes);
		credentialPreview.atType("https://didcomm.org/issue-credential/2.0/credential-preview");

		V10CredentialFreeOfferRequest offer = new V10CredentialFreeOfferRequest();
		offer.setAutoIssue(Boolean.FALSE);
		offer.setAutoRemove(Boolean.FALSE);
		offer.setCredDefId(creDefId);
		offer.setConnectionId(connectionId);
		offer.setTrace(Boolean.TRUE);
		offer.setComment(comment);
		offer.setCredentialPreview(credentialPreview);

		return issueCredentialV10Api.issueCredentialSendOfferPost(offer);
	}

	protected V10CredentialExchange issueCredentialRecord(String credExId, String comment) {
		V10CredentialIssueRequest body = new V10CredentialIssueRequest();
		body.setComment(comment);
		return issueCredentialV10Api.issueCredentialRecordsCredExIdIssuePost(credExId, body);
	}

	public Object revokeCredential(String credExId) {
		RevokeRequest body = new RevokeRequest();
		body.setCredExId(credExId);
		body.setPublish(Boolean.TRUE);
		return revocationApi.revocationRevokePost(body);
	}

	public Object getRevocationStatus(String credExId) {
		return revocationApi.revocationCredentialRecordGet(credExId,null,null);
	}
}
