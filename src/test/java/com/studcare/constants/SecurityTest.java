package com.studcare.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecurityTest {

	@Test
	void testAuthorizationConstant() {
		assertEquals("authorization", Security.AUTHORIZATION);
	}

	@Test
	void testBearerConstant() {
		assertEquals("Bearer ", Security.BEARER);
	}
}
