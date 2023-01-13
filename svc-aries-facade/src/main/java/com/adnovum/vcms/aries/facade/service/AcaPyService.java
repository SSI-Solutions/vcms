package com.adnovum.vcms.aries.facade.service;

import com.adnovum.vcms.common.datamodel.event.ConnectionState;
import com.adnovum.vcms.common.exception.BusinessException;
import com.adnovum.vcms.genapi.acapy.client.dto.ConnRecord;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.CredentialDefinitionsCreatedResult;
import com.adnovum.vcms.genapi.acapy.client.dto.IndyProofReqAttrSpec;
import com.adnovum.vcms.genapi.acapy.client.dto.IndyProofReqAttrSpecNonRevoked;
import com.adnovum.vcms.genapi.acapy.client.dto.IndyProofRequest;
import com.adnovum.vcms.genapi.acapy.client.dto.InvitationResult;
import com.adnovum.vcms.genapi.acapy.client.dto.SchemaGetResult;
import com.adnovum.vcms.genapi.acapy.client.dto.V10CredentialExchange;
import com.adnovum.vcms.genapi.acapy.client.dto.V10PresentationSendRequestRequest;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionInvitation;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.server.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.aries.facade.server.dto.IssuingResponse;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationOption;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.adnovum.vcms.common.exception.BusinessReason.ERROR_CONNECTION_NOT_EXISTENT;
import static com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionState.ESTABLISHED;
import static com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionState.RESPONDED;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcaPyService implements AriesService {

	public static final String ATTRIBUTES_NAME_CRED_DEF_ID = "cred_def_id";

	private final AcaPyClient acaPyClient;

	private final AcaPyProperties acaPyProperties;

	private final ModelMapper modelMapper = new ModelMapper();

	public AriesConnectionInvitation getConnectionInvitation() {
		InvitationResult invitationResult = acaPyClient.createInvitation("");

		AriesConnectionInvitation connectionInvitation = new AriesConnectionInvitation();
		connectionInvitation.setConnectionId(UUID.fromString(Objects.requireNonNull(invitationResult.getConnectionId())));
		connectionInvitation.setInvitationUrl(invitationResult.getInvitationUrl());

		return connectionInvitation;
	}

	public VerificationResponse sendProofRequest(String connectionId, VerificationOption verificationOption) {
		String comment = acaPyProperties.getComment();
		String proofName = acaPyProperties.getProofName();
		String nonce = acaPyProperties.getNonce();

		log.debug("send auth present proof");
		V10PresentationSendRequestRequest presentationSendRequestRequest = generateProofRequest(connectionId,
				verificationOption, comment, proofName, nonce);

		AriesConnectionState connectionState = getConnectionById(connectionId);
		if (ESTABLISHED.equals(connectionState) || RESPONDED.equals(connectionState)) {
			log.debug("auth present proof was sent");
			return modelMapper.map(acaPyClient.presentProofSendRequest(presentationSendRequestRequest),
					VerificationResponse.class);
		}
		log.debug("no auth present proof was sent, because the state object is in wrong state");
		return null;
	}

	private IndyProofReqAttrSpecNonRevoked createNonRevocationPeriodForRightNow() {

		IndyProofReqAttrSpecNonRevoked indyProofReqNonRevoked = new IndyProofReqAttrSpecNonRevoked();
		int currentTimeInSeconds = (int) (System.currentTimeMillis() / 1000);
		// Note: 'from' field could be omitted from the call, but it cannot be set to null.
		// To make sure the REST call is valid we always fill it.
		// Following Aries RFC best practices from = to .
		indyProofReqNonRevoked.setFrom(currentTimeInSeconds);
		indyProofReqNonRevoked.setTo(currentTimeInSeconds);

		return indyProofReqNonRevoked;
	}

	protected V10PresentationSendRequestRequest generateProofRequest(String connectionId, VerificationOption verificationOption,
			String comment, String proofRequestName, String proofRequestNonce) {

		// Predicate Proofs are not yet supported. The field must not be null, so we use an empty map
		Map<String, com.adnovum.vcms.genapi.acapy.client.dto.IndyProofReqPredSpec> requestedPredicates = Collections.emptyMap();

		V10PresentationSendRequestRequest presentationSendRequestRequest = new V10PresentationSendRequestRequest();
		IndyProofRequest proofRequest = new IndyProofRequest();

		Map<String, IndyProofReqAttrSpec> presentProofRevealedAttributesConfiguration = createConfiguration(verificationOption);

		//set the attributes to the present proof object
		proofRequest.setName(proofRequestName);
		proofRequest.setRequestedPredicates(requestedPredicates);
		proofRequest.setVersion("1.0");
		proofRequest.setNonce(proofRequestNonce);
		proofRequest.setNonRevoked(createNonRevocationPeriodForRightNow());

		//set the revealed attributes config to the present proof object
		proofRequest.setRequestedAttributes(presentProofRevealedAttributesConfiguration);

		//set the attributes to the present proof request object
		presentationSendRequestRequest.setComment(comment);
		presentationSendRequestRequest.setConnectionId(UUID.fromString(connectionId));
		presentationSendRequestRequest.setProofRequest(proofRequest);
		presentationSendRequestRequest.setTrace(false);
		return presentationSendRequestRequest;
	}

	private Map<String, IndyProofReqAttrSpec> createConfiguration(VerificationOption verificationOption) {
		//stores the properties for the present proof request
		Map<String, IndyProofReqAttrSpec> presentProofRevealedAttributesConfiguration = new HashMap<>();

		//get the credentialDefinitionId
		String credentialDefinitionId = verificationOption.getCredentialDefinitionId();
		//for each revealed attribute in the config
		for (String attributeNameToReveal : verificationOption.getAttributes()) {
			//stores the attribute for the present proof
			IndyProofReqAttrSpec indyProofReqAttrSpec = new IndyProofReqAttrSpec();
			//add name to the property
			// 'name' field is used for single attributes. 'names' would be useful for attribute group requests.
			// This is not used by VCMS yet.
			indyProofReqAttrSpec.setName(attributeNameToReveal);

			// Set the attribute to non-revoked
			indyProofReqAttrSpec.setNonRevoked(createNonRevocationPeriodForRightNow());

			//add credential definition id as restriction for this attribute
			indyProofReqAttrSpec.setRestrictions(List.of(Map.of(ATTRIBUTES_NAME_CRED_DEF_ID, credentialDefinitionId)));
			//put the property to the revealedAttributes
			presentProofRevealedAttributesConfiguration.put(attributeNameToReveal, indyProofReqAttrSpec);
		}
		return presentProofRevealedAttributesConfiguration;
	}

	public IssuingResponse sendCredentialOffer(String creDefId, UUID connectionId, String comment,
			Map<String, String> claimMap) {
		V10CredentialExchange v10CredentialExchange = acaPyClient.sendCredentialOffer(creDefId, connectionId, comment, claimMap);
		return modelMapper.map(v10CredentialExchange ,IssuingResponse.class);
	}

	public IssuingResponse issueCredentialRecord(String exchangeId, String comment) {
		V10CredentialExchange v10CredentialExchange = acaPyClient.issueCredentialRecord(exchangeId, comment);
		return modelMapper.map(v10CredentialExchange, IssuingResponse.class);
	}

	public void revokeCredential(String credentialExchangeId) {
		acaPyClient.revokeCredential(credentialExchangeId);
	}
	public List<CredentialDefinition> getCredentialDefinitionsFromLedger(List<String> creDefIds) {
		List<CredentialDefinition> credentialDefinitions = new ArrayList<>();
		creDefIds.forEach(x -> credentialDefinitions.add(getCredentialDefinitionFromLedger(x)));
		return credentialDefinitions;
	}

	private CredentialDefinition getCredentialDefinitionFromLedger(String creDefId) {
		CredentialDefinitionGetResult credentialDefinitionGetResults = acaPyClient.getCredentialDefinitionById(creDefId);

		CredentialDefinition credentialDefinition = new CredentialDefinition();
		if (credentialDefinitionGetResults.getCredentialDefinition() != null) {
			credentialDefinition.setCredentialDefinitionId(credentialDefinitionGetResults.getCredentialDefinition().getId());
			String schemaId = credentialDefinitionGetResults.getCredentialDefinition().getSchemaId();
			SchemaGetResult schemaGetResults = acaPyClient.getSchemaById(schemaId);
			assert schemaGetResults.getSchema() != null;
			credentialDefinition.setClaims(schemaGetResults.getSchema().getAttrNames());
		}
		return credentialDefinition;
	}

	public List<CredentialDefinition> getCreatedCredentialDefinitions() {
		List<String> creDefIds = getCreatedCreDefIds();
		List<CredentialDefinition> credentialDefinitions = new ArrayList<>();
		creDefIds.forEach(x -> credentialDefinitions.add(getCredentialDefinitionFromLedger(x)));
		return credentialDefinitions;
	}

	private List<String> getCreatedCreDefIds() {
		CredentialDefinitionsCreatedResult credentialDefinitionsCreatedResults = acaPyClient.getCreatedCredentialDefinitions();
		List<String> creDefIds = new ArrayList<>();
		if (credentialDefinitionsCreatedResults.getCredentialDefinitionIds() != null) {
			creDefIds.addAll(credentialDefinitionsCreatedResults.getCredentialDefinitionIds());
		}
		return creDefIds;
	}

	public Boolean isCreDefIdCreatedByThisWallet(String creDefId) {
		List<String> creDefIds = getCreatedCreDefIds();
		return creDefIds.contains(creDefId);
	}

	public AriesConnectionState getConnectionById(String connectionId) {
		ConnRecord connRecord = acaPyClient.getConnectionById(connectionId);
		if (connRecord == null) {
			String msg = String.format("Connection %s not found in wallet.", connectionId);
			throw new BusinessException(msg, ERROR_CONNECTION_NOT_EXISTENT);
		}
		return ConnectionState.fromValue(connRecord.getState()).getAriesConnectionState();
	}
}
