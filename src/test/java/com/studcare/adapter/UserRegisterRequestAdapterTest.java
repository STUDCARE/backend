package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpRequestData;
import com.studcare.model.UserDTO;
import com.studcare.model.UserRegisterRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserRegisterRequestAdapter.class})
class UserRegisterRequestAdapterTest {

	@Autowired
	private UserRegisterRequestAdapter userRegisterRequestAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		Map<String, String> headers = new HashMap<>();
		headers.put("content type", "application-json");
		httpRequestData.setHeaders(headers);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("query-params", "test-param");
		httpRequestData.setQueryParams(queryParams);
		String requestBody = "{\"email\":\"user@gmail.com\"}";
		httpRequestData.setRequestBody(requestBody);

		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("user@gmail.com");

		when(objectMapper.readValue(requestBody, UserDTO.class)).thenReturn(userDTO);

		UserRegisterRequestDTO userRegisterRequestDTO = userRegisterRequestAdapter.adapt(httpRequestData);

		assertEquals(headers, userRegisterRequestDTO.getHeaders());
		assertEquals(queryParams, userRegisterRequestDTO.getQueryParams());
		assertEquals("user@gmail.com", userRegisterRequestDTO.getUserDTO().getEmail());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setRequestBody("invalid-json");
		when(objectMapper.readValue("invalid-json", UserDTO.class)).thenThrow(new JsonProcessingException("error") {});
		assertThrows(StudCareRuntimeException.class, () -> userRegisterRequestAdapter.adapt(httpRequestData));
	}
}
