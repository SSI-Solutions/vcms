package com.adnovum.vcms.common.exception;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public enum TechnicalReason implements Reason {

	ERROR_TECHNICAL("res.error.technical", 1),
	ERROR_CONNECTIVITY("res.error.connectivity", 2),
	ERROR_BACKEND_SERVICE_CALL("res.error.svc2svc.call", 3);

	private final String reason;

	private final int errorCode;

	TechnicalReason(String reason, int errorCode) {
		this.reason = reason;
		this.errorCode = errorCode;
	}

	@Override
	public String getReason() {
		return reason;
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("errorCode", errorCode);
		tsb.append("reason", reason);
		return tsb.toString();
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}
}
