package com.studcare.util;

import com.studcare.model.HttpResponseData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilsTest {

	@Test
	void isEmpty_allEmptyParameters() {
		assertTrue(CommonUtils.isEmpty("", null, "  "));
	}

	@Test
	void isEmpty_noParameters() {
		assertFalse(CommonUtils.isEmpty());
	}

	@Test
	void createResponseEntity_withHeaders() {
		HttpResponseData responseData = new HttpResponseData();
		responseData.setHttpStatus(HttpStatus.OK);
		responseData.setResponseBody("response body");
		Map<String, String> headers = new HashMap<>();
		headers.put("Header1", "Value1");
		headers.put("Header2", "Value2");
		responseData.setHeaders(headers);

		ResponseEntity<Object> responseEntity = CommonUtils.createResponseEntity(responseData);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("response body", responseEntity.getBody());
		HttpHeaders responseHeaders = responseEntity.getHeaders();
		assertEquals(2, responseHeaders.size());
		assertEquals("Value1", responseHeaders.getFirst("Header1"));
		assertEquals("Value2", responseHeaders.getFirst("Header2"));
	}

	@Test
	void createResponseEntity_withoutHeaders() {
		HttpResponseData responseData = new HttpResponseData();
		responseData.setHttpStatus(HttpStatus.OK);
		responseData.setResponseBody("response body");

		ResponseEntity<Object> responseEntity = CommonUtils.createResponseEntity(responseData);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("response body", responseEntity.getBody());
		HttpHeaders responseHeaders = responseEntity.getHeaders();
		assertTrue(ObjectUtils.isEmpty(responseHeaders));
	}
}
