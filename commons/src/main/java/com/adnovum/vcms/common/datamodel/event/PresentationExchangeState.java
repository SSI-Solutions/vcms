package com.adnovum.vcms.common.datamodel.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PresentationExchangeState {

	// Based on https://github.com/hyperledger/aries-cloudagent-python/blob/main/aries_cloudagent/protocols/present_proof/v1_0/models/presentation_exchange.py

	PROPOSAL_SENT("proposal_sent"),

	PROPOSAL_RECEIVED("proposal_received"),

	REQUEST_SENT("request_sent"),

	REQUEST_RECEIVED("request_received"),

	PRESENTATION_SENT("presentation_sent"),

	PRESENTATION_RECEIVED("presentation_received"),

	VERIFIED("verified"),

	PRESENTATION_ACKED("presentation_acked"),

	STATE_ABANDONED("abandoned");


	private final String value;

	PresentationExchangeState(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static PresentationExchangeState fromValue(String value) {
		for (PresentationExchangeState b : PresentationExchangeState.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Cannot map acapy state '" + value + "' to PresentationExchangeState");
	}
}
