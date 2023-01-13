package com.adnovum.vcms.connector.datamodel;

import com.adnovum.vcms.genapi.connector.server.dto.ConnStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ConnectionProcessState {

	CONNECTION_INITIATING (
			ConnStatus.INIT
	),

	CONNECTION_STATE_CREATED (
			ConnStatus.CREATED
	),

	CONNECTION_REQUEST_SENT(
			ConnStatus.REQUESTED
	),

	CONNECTION_RESPONSE_RECEIVED(
			ConnStatus.RESPONDED
	),

	CONNECTION_ESTABLISHED(
			ConnStatus.ESTABLISHED
	),

	CONNECTION_CLOSED(
			ConnStatus.ABANDONED
	);

	@Getter
	private final ConnStatus connStatus;
}
