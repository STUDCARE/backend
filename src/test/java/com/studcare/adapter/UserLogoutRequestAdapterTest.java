package com.studcare.adapter;

import com.studcare.model.HttpRequestData;
import com.studcare.model.LogoutRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {UserLogoutRequestAdapter.class})
class UserLogoutRequestAdapterTest {

	@Autowired
	private UserLogoutRequestAdapter userLogoutRequestAdapter;

	@Test
	void adaptSuccess() {
		HttpRequestData httpRequestData = new HttpRequestData();
		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application-json");
		httpRequestData.setHeaders(headers);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("query-params", "test-param");
		httpRequestData.setQueryParams(queryParams);
		httpRequestData.setReference("user@gmail.com");

		LogoutRequestDTO expectedLogoutRequestDTO = new LogoutRequestDTO();
		expectedLogoutRequestDTO.setHeaders(headers);
		expectedLogoutRequestDTO.setQueryParams(queryParams);
		expectedLogoutRequestDTO.setEmail("user@gmail.com");

		LogoutRequestDTO actualLogoutRequestDTO = userLogoutRequestAdapter.adapt(httpRequestData);

		assertEquals(expectedLogoutRequestDTO.getHeaders(), actualLogoutRequestDTO.getHeaders());
		assertEquals(expectedLogoutRequestDTO.getQueryParams(), actualLogoutRequestDTO.getQueryParams());
		assertEquals(expectedLogoutRequestDTO.getEmail(), actualLogoutRequestDTO.getEmail());
	}
}
