package com.adnovum.vcms.common.datamodel.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CredentialExchangeState {

	// Based on https://github.com/hyperledger/aries-cloudagent-python/blob/main/aries_cloudagent/protocols/issue_credential/v1_0/models/credential_exchange.py

	PROPOSAL_SENT("proposal_sent"),

	PROPOSAL_RECEIVED("proposal_received"),

	OFFER_SENT("offer_sent"),

	OFFER_RECEIVED("offer_received"),

	REQUEST_SENT("request_sent"),

	REQUEST_RECEIVED("request_received"),

	CREDENTIAL_ISSUED("credential_issued"),

	CREDENTIAL_RECEIVED("credential_received"),

	CREDENTIAL_ACKED("credential_acked"),

	CREDENTIAL_REVOKED("credential_revoked"),

	STATE_ABANDONED("abandoned");

	private final String value;


	CredentialExchangeState(String value) {
		this.value = value;
	}

	@JsonCreator
	public static CredentialExchangeState fromValue(String value) {
		for (CredentialExchangeState b : CredentialExchangeState.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Cannot map acapy state '" + value + "' to CredentialExchangeState");
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
