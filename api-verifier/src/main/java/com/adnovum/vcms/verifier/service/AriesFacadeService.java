package com.adnovum.vcms.verifier.service;

import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationOption;
import com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse;
import com.adnovum.vcms.genapi.verifier.server.dto.CredentialDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AriesFacadeService {

	private final VerifierConfiguration verifierConfiguration;

	private final AriesFacadeClient ariesFacadeClient;

	private final ModelMapper modelMapper = new ModelMapper();

	public List<CredentialDefinition> getVerifierCredentialDefinitionsFromLedger() {
		List<CredentialDefinition> credentialDefinitions = new ArrayList<>();
		getCredentialDefinitionsByIds(verifierConfiguration.getCreDefIds())
				.forEach(x -> credentialDefinitions.add(modelMapper.map(x, CredentialDefinition.class)));
		ariesFacadeClient.getCredentialDefinitionsByCreation()
				.forEach(x -> credentialDefinitions.add(modelMapper.map(x, CredentialDefinition.class)));
		return credentialDefinitions.stream().distinct().collect(Collectors.toList());
	}

	public List<CredentialDefinition> getCredentialDefinitionsByIds(List<String> creDefIds) {

		List<CredentialDefinition> foundCredentialDefinitions = ariesFacadeClient.getCredentialDefinitionsById(creDefIds)
				.stream().map(x -> modelMapper.map(x, CredentialDefinition.class)).collect(Collectors.toList());
		List<String> foundIds = foundCredentialDefinitions.stream().map(CredentialDefinition::getCredentialDefinitionId)
				.filter(Objects::nonNull).collect(Collectors.toList());
		List<String> missingCredentialDefinitionIds = creDefIds.stream()
				.filter(x -> !foundIds.contains(x)).collect(Collectors.toList());

		if (!missingCredentialDefinitionIds.isEmpty()) {
			String error = "The following configured credential definition ids " + missingCredentialDefinitionIds
					+ " cannot be found on the ledger. Please check your Ledger configuration"
					+ " or your credential definition ids in the proof configuration.";
			log.error(error);
		}
		return foundCredentialDefinitions;
	}

	public String presentProofSendRequest(UUID connectionId, String creDefId, List<String> attributes) {
		VerificationOption verificationOption = new VerificationOption();
		verificationOption.setCredentialDefinitionId(creDefId);
		verificationOption.setAttributes(attributes);
		VerificationResponse verificationResponse = ariesFacadeClient.presentProofSendRequest(connectionId, verificationOption);
		return verificationResponse.getPresentationExchangeId();
	}

	public AriesConnectionState getConnectionById(UUID connectionId) {
		return ariesFacadeClient.getConnectionState(connectionId);
	}
}
