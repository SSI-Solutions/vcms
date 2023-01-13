package com.adnovum.vcms.issuer.datamodel.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RevocationState {

	INACTIVE("inactive"),

	ISSUED("issued"),

	REVOKED("revoked");

	private final String value;

	RevocationState(String value) {
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
	public static RevocationState fromValue(String value) {
		for (RevocationState b : RevocationState.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}
