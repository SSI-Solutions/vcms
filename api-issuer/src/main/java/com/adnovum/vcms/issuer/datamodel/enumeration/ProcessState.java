package com.adnovum.vcms.issuer.datamodel.enumeration;

import com.adnovum.vcms.genapi.issuer.server.dto.IssuStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ProcessState {

	PROCESS_CREATED(
			"process_created",
			IssuStatus.IN_PROGRESS
	),

	CREDENTIAL_OFFER_SENT(
			"credential_offer_sent",
			IssuStatus.IN_PROGRESS
	),

	CREDENTIAL_REQUEST_RECEIVED(
			"credential_request_received",
			IssuStatus.IN_PROGRESS
	),

	CREDENTIAL_ISSUED(
			"credential_issued",
			IssuStatus.VC_ISSUED
	),

	CREDENTIAL_REVOKED(
			"credential_revoked",
			IssuStatus.REVOKED
	),

	STATE_ABANDONED(
			"credential_revoked",
			IssuStatus.ABANDONED
	);

	@Getter
	private final String value;

	@Getter
	private final IssuStatus issuStatus;

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ProcessState fromValue(String value) {
		for (ProcessState b : ProcessState.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}
