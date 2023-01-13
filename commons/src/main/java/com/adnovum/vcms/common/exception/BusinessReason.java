package com.adnovum.vcms.common.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public enum BusinessReason implements Reason {

	// general Exception when you can not find a better one -> blame the code 1000 ...
	ERROR_IMPLEMENTATION("res.error.impl", 1000),

	// error in authentication / not allowed 2000 ...
	ERROR_NOT_ALLOWED("res.error.authentication.not.allowed", 2000),

	// reasons for forbidden operations 3000 ...
	ERROR_FORBIDDEN_OPERATION("res.error.forbidden.operation", 3000),
	ERROR_FORBIDDEN_HOLDER("res.error.holder.not.whitelisted", 3010),

	// use this reason if you get an unexpected data constellation 4000 ...
	ERROR_DATA_INVALID_CONSTELLATION("res.error.data.invalid.constellation", 4000),

	// use this reason if you got into troubles because of bad input from anywhere 5000 ...
	ERROR_INPUT_VALIDATION_FAILED("res.error.input.validation.failed", 5000),

	// bad input is an identifier for a resource which can not be found by the system 6000 ...
	ERROR_HOLDER_NOT_EXISTENT("res.error.holder.not.existent", 6000),
	ERROR_CONNECTION_NOT_EXISTENT("res.error.connection.not.existent", 6010),

	ERROR_CREDENTIAL_NOT_EXISTENT("res.error.credential.not.existent", 6020),
	ERROR_AUTH_STATE_NOT_EXISTENT("res.error.auth.state.not.existent", 6030),

	ERROR_VERIFICATION_STATE_NOT_EXISTENT("res.error.verification.state.not.existent", 6040),

	ERROR_ISSUING_STATE_NOT_EXISTENT("res.error.verification.state.not.existent", 6050),

	ERROR_CREDENTIAL_NOT_VALID("res.error.credential.definition.not.valid", 6060),

	// this is the reason if a subsystem failed. For example one of the webservices returned an error 7000 ...
	ERROR_IN_SUBSYSTEM("res.error.in.subsystem", 7000);

	private final String reason;

	private final int errorCode;

	BusinessReason(String reason, int errorCode) {
		this.reason = reason;
		this.errorCode = errorCode;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public String toString() {
		var tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("errorCode", errorCode);
		tsb.append("reason", reason);
		return tsb.toString();
	}

	public int getErrorCode() {
		return errorCode;
	}
}
