package com.studcare.adapter;

import com.studcare.model.HttpRequestData;
import com.studcare.model.UserDeletionRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {UserDeletionRequestAdapter.class})
class UserDeletionRequestAdapterTest {

	@Autowired
	private UserDeletionRequestAdapter userDeletionRequestAdapter;

	@Test
	void adaptSuccess() {
		// Prepare the input data
		HttpRequestData httpRequestData = new HttpRequestData();
		Map<String, String> headers = new HashMap<>();
		headers.put("content type", "application-json");
		httpRequestData.setHeaders(headers);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("query-params", "test-param");
		httpRequestData.setQueryParams(queryParams);
		httpRequestData.setReference("user@gmail.com");

		// Call the method under test
		UserDeletionRequestDTO userDeletionRequestDTO = userDeletionRequestAdapter.adapt(httpRequestData);

		// Verify the results
		assertEquals(headers, userDeletionRequestDTO.getHeaders());
		assertEquals(queryParams, userDeletionRequestDTO.getQueryParams());
		assertEquals("user@gmail.com", userDeletionRequestDTO.getUserEmail());
	}

	@Test
	void adaptWithNullReference() {
		// Prepare the input data with null reference
		HttpRequestData httpRequestData = new HttpRequestData();
		Map<String, String> headers = new HashMap<>();
		headers.put("content type", "application-json");
		httpRequestData.setHeaders(headers);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("query-params", "test-param");
		httpRequestData.setQueryParams(queryParams);
		httpRequestData.setReference(null);

		// Call the method under test
		UserDeletionRequestDTO userDeletionRequestDTO = userDeletionRequestAdapter.adapt(httpRequestData);

		// Verify the results
		assertEquals(headers, userDeletionRequestDTO.getHeaders());
		assertEquals(queryParams, userDeletionRequestDTO.getQueryParams());
		assertNull(userDeletionRequestDTO.getUserEmail());
	}
}
