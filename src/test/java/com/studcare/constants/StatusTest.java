package com.studcare.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusTest {

	@Test
	void testStatusEnum() {
		// Test SUCCESS enum value
		Status successStatus = Status.SUCCESS;
		assertEquals("Success", successStatus.getDescription());

		// Test FAILURE enum value
		Status failureStatus = Status.FAILURE;
		assertEquals("Failure", failureStatus.getDescription());
	}
}
