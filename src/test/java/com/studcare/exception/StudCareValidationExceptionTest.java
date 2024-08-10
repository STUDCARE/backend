package com.studcare.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudCareValidationExceptionTest {

	@Test
	void testExceptionWithMessage() {
		String message = "Validation error occurred";
		StudCareValidationException exception = new StudCareValidationException(message);

		assertEquals(message, exception.getMessage());
		assertEquals(StudCareValidationException.ERROR_CODE_DEFAULT, exception.getCode());
	}

	@Test
	void testExceptionWithCodeAndMessage() {
		int code = StudCareValidationException.ERROR_CODE_INVALID_PROCESS;
		String message = "Validation error occurred with invalid process code";
		StudCareValidationException exception = new StudCareValidationException(code, message);

		assertEquals(message, exception.getMessage());
		assertEquals(code, exception.getCode());
	}

	@Test
	void testExceptionWithCause() {
		Throwable cause = new RuntimeException("Root cause of validation error");
		StudCareValidationException exception = new StudCareValidationException(cause);

		assertEquals(cause, exception.getCause());
		assertEquals(StudCareValidationException.ERROR_CODE_DEFAULT, exception.getCode());
	}

	@Test
	void testExceptionWithMessageAndCause() {
		String message = "Validation error occurred";
		Throwable cause = new RuntimeException("Root cause of validation error");
		StudCareValidationException exception = new StudCareValidationException(message, cause);

		assertEquals(message, exception.getMessage());
		assertEquals(cause, exception.getCause());
		assertEquals(StudCareValidationException.ERROR_CODE_DEFAULT, exception.getCode());
	}
}
