package com.adnovum.vcms.issuer.service;

import com.adnovum.vcms.common.service.AriesFacadeClient;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.client.dto.IssuingOption;
import com.adnovum.vcms.genapi.aries.facade.client.dto.IssuingResponse;
import com.adnovum.vcms.genapi.issuer.server.dto.CredentialDefinition;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AriesFacadeService {

	private final AriesFacadeClient ariesFacadeClient;

	private final ModelMapper modelMapper = new ModelMapper();

	public List<CredentialDefinition> getCreatedCredentialDefinitions() {
		List<CredentialDefinition> credentialDefinitions = new ArrayList<>();
		ariesFacadeClient.getCredentialDefinitionsByCreation()
				.forEach(x -> credentialDefinitions.add(modelMapper.map(x, CredentialDefinition.class)));
		return credentialDefinitions;
	}

	public Boolean isCreatedCreDef(String creDefId) {
		return ariesFacadeClient.isCreatedCreDef(creDefId);
	}

	public String offerCredential(UUID connectionId, String creDefId, Map<String, String> claims) {
		IssuingOption issuingOption = new IssuingOption();
		issuingOption.setCredentialDefinitionId(creDefId);
		issuingOption.setAttributes(claims);
		return ariesFacadeClient.offerCredential(connectionId, issuingOption).getCredentialExchangeId();
	}

	public IssuingResponse issueCredential(UUID exchangeId) {
		return ariesFacadeClient.issueCredential(exchangeId);
	}

	public void revokeCredential(String creExchId) {
		ariesFacadeClient.revokeCredential(creExchId);
	}

	public AriesConnectionState getConnectionStateById(UUID connectionId) {
		return ariesFacadeClient.getConnectionState(connectionId);
	}
}
