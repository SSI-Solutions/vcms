package com.adnovum.vcms.common.service;

import com.adnovum.vcms.genapi.aries.facade.client.ApiClient;
import com.adnovum.vcms.genapi.aries.facade.client.controller.CheckApi;
import com.adnovum.vcms.genapi.aries.facade.client.controller.ConnectionApi;
import com.adnovum.vcms.genapi.aries.facade.client.controller.IssuingApi;
import com.adnovum.vcms.genapi.aries.facade.client.controller.RevocationApi;
import com.adnovum.vcms.genapi.aries.facade.client.controller.VerificationApi;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionInvitation;
import com.adnovum.vcms.genapi.aries.facade.client.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.client.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.aries.facade.client.dto.IssuingOption;
import com.adnovum.vcms.genapi.aries.facade.client.dto.IssuingResponse;
import com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationOption;
import com.adnovum.vcms.genapi.aries.facade.client.dto.VerificationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AriesFacadeClient {

	private final ConnectionApi connectionApi;

	private final CheckApi checkApi;

	private final VerificationApi verificationApi;

	private final IssuingApi issuingApi;

	private final RevocationApi revokeApi;

	public AriesFacadeClient(AriesFacadeProperties controllerProperties) {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(controllerProperties.getBasePath());
		connectionApi = new ConnectionApi(apiClient);
		checkApi = new CheckApi(apiClient);
		verificationApi = new VerificationApi(apiClient);
		issuingApi = new IssuingApi(apiClient);
		revokeApi = new RevocationApi(apiClient);
	}

	public List<CredentialDefinition> getCredentialDefinitionsByCreation() {
		return checkApi.getCreatedCredentialDefinitions();
	}

	public List<CredentialDefinition> getCredentialDefinitionsById(List<String> creDefIds) {
		return checkApi.getCredentialDefinitionsById(creDefIds);
	}

	public Boolean isCreatedCreDef(String creDefId) {
		return checkApi.isCreatedCredentialDefinition(creDefId);
	}

	public VerificationResponse presentProofSendRequest(UUID connectionId, VerificationOption verificationOption) {
		return verificationApi.verifyCredentials(connectionId, verificationOption);
	}

	public IssuingResponse offerCredential(UUID connectionId, IssuingOption issuingOption) {
		return issuingApi.offerCredential(connectionId, issuingOption);
	}

	public IssuingResponse issueCredential(UUID exchangeId) {
		return issuingApi.issueCredential(exchangeId);
	}

	public AriesConnectionInvitation getInvitation() {
		return connectionApi.connectionInvitation();
	}

	public AriesConnectionState getConnectionState(UUID connectionId){
		return connectionApi.connectionState(connectionId);
	}

	public void revokeCredential(String creExchId) { revokeApi.revokeCredential(creExchId);	}
}
