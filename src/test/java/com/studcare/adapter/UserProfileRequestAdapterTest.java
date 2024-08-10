package com.studcare.adapter;

import com.studcare.model.HttpRequestData;
import com.studcare.model.UserProfileRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {UserProfileRequestAdapter.class})
class UserProfileRequestAdapterTest {

	@Autowired
	private UserProfileRequestAdapter userProfileRequestAdapter;

	@Test
	void adaptSuccess() {
		HttpRequestData httpRequestData = new HttpRequestData();
		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application-json");
		httpRequestData.setHeaders(headers);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("query-params", "test-param");
		httpRequestData.setQueryParams(queryParams);
		httpRequestData.setReference("user@example.com");

		UserProfileRequestDTO userProfileRequestDTO = userProfileRequestAdapter.adapt(httpRequestData);

		assertEquals(headers, userProfileRequestDTO.getHeaders());
		assertEquals(queryParams, userProfileRequestDTO.getQueryParams());
		assertEquals("user@example.com", userProfileRequestDTO.getUserEmail());
	}
}
