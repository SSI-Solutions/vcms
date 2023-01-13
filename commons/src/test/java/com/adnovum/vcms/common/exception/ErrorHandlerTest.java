package com.adnovum.vcms.common.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ErrorHandlerTest {

	private ErrorHandler errorHandler;

	@BeforeEach
	void init() {
		errorHandler = new ErrorHandler();
	}

	@Test
	void checkHttpStatusResponseFor1000ErrorCodes() {
		BusinessException exception = new BusinessException("checkHttpStatusResponseFor100ErrorCodes",
				BusinessReason.ERROR_IMPLEMENTATION);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
	}

	@Test
	void checkHttpStatusResponseFor2000ErrorCodes() {
		BusinessException exception = new BusinessException("checkHttpStatusResponseFor100ErrorCodes",
				BusinessReason.ERROR_NOT_ALLOWED);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	void checkHttpStatusResponseFor3000ErrorCodes() {
		BusinessException exception = new BusinessException("checkHttpStatusResponseFor3000ErrorCodes",
				BusinessReason.ERROR_FORBIDDEN_OPERATION);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	void checkHttpStatusResponseFor4000ErrorCodes() {
		BusinessException exception = new BusinessException("checkHttpStatusResponseFor4000ErrorCodes",
				BusinessReason.ERROR_DATA_INVALID_CONSTELLATION);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	void checkHttpStatusResponseFor5000ErrorCodes() {
		BusinessException exception = new BusinessException("checkHttpStatusResponseFor5000ErrorCodes",
				BusinessReason.ERROR_INPUT_VALIDATION_FAILED);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void checkHttpStatusResponseFor6000ErrorCodes() {
		BusinessException exception = new BusinessException("checkHttpStatusResponseFor6000ErrorCodes",
				BusinessReason.ERROR_HOLDER_NOT_EXISTENT);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void checkHttpStatusResponseFor7000ErrorCodes() {
		BusinessException exception = new BusinessException("Test", BusinessReason.ERROR_IN_SUBSYSTEM);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	void checkTechnicalException() {
		TechnicalException exception = new TechnicalException("Test", TechnicalReason.ERROR_TECHNICAL);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleTechnicalException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	void checkTechnicalExceptionWithThrowable() {
		TechnicalException exception = new TechnicalException("Test", TechnicalReason.ERROR_TECHNICAL, new RuntimeException());
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleTechnicalException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	void checkBusinessExceptionWithThrowable() {
		BusinessException exception = new BusinessException("Test", BusinessReason.ERROR_INPUT_VALIDATION_FAILED,
				new RuntimeException());
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.handleBusinessException(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void checkHttpMessageNotReadableException() {
		HttpMessageNotReadableException exception = new HttpMessageNotReadableException("checkHttpMessageNotReadableException",
				Mockito.mock(HttpInputMessage.class));
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.dtoEnumValidationFailed(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void checkMethodArgumentNotValidException() {
		String test = "checkMethodArgumentNotValidException";
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		when(bindingResult.getFieldError()).thenReturn(new FieldError(test, test, test));
		MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
		MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
		ResponseEntity<ReasonBasedExceptionDto> response = errorHandler.dtoValidationFailed(exception);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}
