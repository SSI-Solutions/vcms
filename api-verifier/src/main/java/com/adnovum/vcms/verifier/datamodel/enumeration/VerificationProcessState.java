package com.adnovum.vcms.verifier.datamodel.enumeration;

import com.adnovum.vcms.genapi.verifier.server.dto.VerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum VerificationProcessState {

	VERIFICATION_STATE_CREATED (
			"Proof request is sent to your wallet.",
			VerStatus.IN_PROGRESS
	),

	VERIFICATION_REQUEST_SENT(
			"Proof request is sent to your wallet.",
			VerStatus.IN_PROGRESS
	),

	VERIFICATION_PRESENTATION_RECEIVED(
			"",
			VerStatus.IN_PROGRESS
	),

	PRESENTATION_VERIFIED(
			"Your credentials are verified.",
			VerStatus.VERIFIED
	),

	CREDENTIAL_REVOKED(
			"The credential is revoked and could not be verified",
			VerStatus.REVOKED
	),

	STATE_ABANDONED(
			"The credential is revoked and could not be verified",
			VerStatus.REVOKED
	);

	@Getter
	private final String description;

	@Getter
	private final VerStatus verStatus;
}
