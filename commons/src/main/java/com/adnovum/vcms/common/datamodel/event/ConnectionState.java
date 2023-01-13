package com.adnovum.vcms.common.datamodel.event;

import com.adnovum.vcms.genapi.aries.facade.server.dto.AriesConnectionState;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum ConnectionState {
	INIT("init", AriesConnectionState.INIT),

	START("start", AriesConnectionState.INIT),

	INVITATION("invitation", AriesConnectionState.CREATED),

	REQUEST_SENT("request", AriesConnectionState.REQUESTED),

	RESPONSE_RECEIVED("response", AriesConnectionState.RESPONDED),

	COMPLETED("completed", AriesConnectionState.ESTABLISHED),

	ACTIVE("active", AriesConnectionState.ESTABLISHED),

	ERROR("error", AriesConnectionState.ABANDONED),

	ABANDONED("abandoned", AriesConnectionState.ABANDONED);

	private final String value;

	private final AriesConnectionState ariesConnectionState;

	@JsonCreator
	public static ConnectionState fromValue(String value) {
		for (ConnectionState b : ConnectionState.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Cannot map acapy state '" + value + "' to ConnectionState");
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@JsonValue
	public AriesConnectionState getAriesConnectionState() {
		return ariesConnectionState;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
