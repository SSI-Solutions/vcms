package com.adnovum.vcms.common.exception;


public class BusinessException extends ReasonBasedException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String message, BusinessReason reason, Throwable cause) {
		super(message, reason, cause);
	}

	public BusinessException(String message, BusinessReason reason) {
		super(message, reason);
	}
}
