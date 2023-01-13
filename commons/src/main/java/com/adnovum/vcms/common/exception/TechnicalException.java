package com.adnovum.vcms.common.exception;

public class TechnicalException extends ReasonBasedException {

	private static final long serialVersionUID = 1L;

	public TechnicalException(String message, TechnicalReason reason, Throwable cause) {
		super(message, reason, cause);
	}

	public TechnicalException(String message, TechnicalReason reason) {
		super(message, reason);
	}
}
