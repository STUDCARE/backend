package com.studcare.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudCareRuntimeExceptionTest {

	@Test
	void testExceptionWithMessage() {
		String message = "An error occurred";
		StudCareRuntimeException exception = new StudCareRuntimeException(message);

		assertEquals(message, exception.getMessage());
		assertEquals(StudCareRuntimeException.ERROR_CODE_DEFAULT, exception.getCode());
	}

	@Test
	void testExceptionWithCodeAndMessage() {
		int code = 123;
		String message = "An error occurred with code";
		StudCareRuntimeException exception = new StudCareRuntimeException(code, message);

		assertEquals(message, exception.getMessage());
		assertEquals(code, exception.getCode());
	}

	@Test
	void testExceptionWithCause() {
		Throwable cause = new RuntimeException("Root cause");
		StudCareRuntimeException exception = new StudCareRuntimeException(cause);

		assertEquals(cause, exception.getCause());
		assertEquals(StudCareRuntimeException.ERROR_CODE_DEFAULT, exception.getCode());
	}

	@Test
	void testExceptionWithMessageAndCause() {
		String message = "An error occurred";
		Throwable cause = new RuntimeException("Root cause");
		StudCareRuntimeException exception = new StudCareRuntimeException(message, cause);

		assertEquals(message, exception.getMessage());
		assertEquals(cause, exception.getCause());
		assertEquals(StudCareRuntimeException.ERROR_CODE_DEFAULT, exception.getCode());
	}
}
