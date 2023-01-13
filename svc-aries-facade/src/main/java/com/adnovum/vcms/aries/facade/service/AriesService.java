package com.adnovum.vcms.aries.facade.service;

import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionInvitation;
import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionState;
import com.adnovum.vcms.genapi.aries.facade.server.dto.CredentialDefinition;
import com.adnovum.vcms.genapi.aries.facade.server.dto.IssuingResponse;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationOption;
import com.adnovum.vcms.genapi.aries.facade.server.dto.VerificationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AriesService {

	AriesConnectionInvitation getConnectionInvitation();

	VerificationResponse sendProofRequest(String connectionId, VerificationOption verificationOption);

	IssuingResponse sendCredentialOffer(String credfDefId, UUID connectionId, String comment, Map<String, String> claimMap);

	IssuingResponse issueCredentialRecord(String exchangeId, String comment);

	void revokeCredential(String credentialExchangeId);

	List<CredentialDefinition> getCredentialDefinitionsFromLedger(List<String> creDefIds);

	List<CredentialDefinition> getCreatedCredentialDefinitions();

	Boolean isCreDefIdCreatedByThisWallet(String creDefId);

	AriesConnectionState getConnectionById(String connectionId);
}
