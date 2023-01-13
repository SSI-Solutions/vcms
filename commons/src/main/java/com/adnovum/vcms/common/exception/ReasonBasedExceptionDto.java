package com.adnovum.vcms.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for a specific error when an operation can not be completed successfully.")
public class ReasonBasedExceptionDto {

	@Schema(description = "The code of the error.")
	private Integer errorCode;

	@Schema(description = "The reason for the error.")
	private String reason;

	@Schema(description = "The message containing why the error was returned by the application.")
	private String message;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
