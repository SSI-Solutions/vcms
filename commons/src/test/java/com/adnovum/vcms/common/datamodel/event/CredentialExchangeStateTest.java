package com.adnovum.vcms.common.datamodel.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CredentialExchangeStateTest {

	@Test
	void checkMappings() {
		assertThat(CredentialExchangeState.fromValue("proposal_sent")).isEqualTo(CredentialExchangeState.PROPOSAL_SENT);
		assertThat(CredentialExchangeState.fromValue("proposal_received")).isEqualTo(CredentialExchangeState.PROPOSAL_RECEIVED);
		assertThat(CredentialExchangeState.fromValue("offer_sent")).isEqualTo(CredentialExchangeState.OFFER_SENT);
		assertThat(CredentialExchangeState.fromValue("offer_received")).isEqualTo(CredentialExchangeState.OFFER_RECEIVED);
		assertThat(CredentialExchangeState.fromValue("request_sent")).isEqualTo(CredentialExchangeState.REQUEST_SENT);
		assertThat(CredentialExchangeState.fromValue("request_received")).isEqualTo(CredentialExchangeState.REQUEST_RECEIVED);
		assertThat(CredentialExchangeState.fromValue("credential_issued")).isEqualTo(CredentialExchangeState.CREDENTIAL_ISSUED);
		assertThat(CredentialExchangeState.fromValue("credential_received")).isEqualTo(
				CredentialExchangeState.CREDENTIAL_RECEIVED);
		assertThat(CredentialExchangeState.fromValue("credential_acked")).isEqualTo(CredentialExchangeState.CREDENTIAL_ACKED);
		assertThat(CredentialExchangeState.fromValue("credential_revoked")).isEqualTo(CredentialExchangeState.CREDENTIAL_REVOKED);
		assertThat(CredentialExchangeState.fromValue("abandoned")).isEqualTo(CredentialExchangeState.STATE_ABANDONED);
	}
}
