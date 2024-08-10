package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.LogoutResponseDTO;
import com.studcare.model.UserProfileResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserLogoutResponseAdapter.class})
class UserLogoutResponseAdapterTest {

	@Autowired
	private UserLogoutResponseAdapter userLogoutResponseAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO();
		logoutResponseDTO.setResponseCode(Status.SUCCESS);

		String responseBody = "{\"message\":\"Logout successful\"}";
		when(objectMapper.writeValueAsString(logoutResponseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = userLogoutResponseAdapter.adapt(logoutResponseDTO);

		assertEquals(HttpStatus.OK, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptFailure() throws JsonProcessingException {
		LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO();
		logoutResponseDTO.setResponseCode(Status.FAILURE);

		String responseBody = "{\"message\":\"Logout failed\"}";
		when(objectMapper.writeValueAsString(logoutResponseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = userLogoutResponseAdapter.adapt(logoutResponseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		LogoutResponseDTO logoutResponseDTO = new LogoutResponseDTO();
		logoutResponseDTO.setResponseCode(Status.SUCCESS);

		when(objectMapper.writeValueAsString(logoutResponseDTO)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> userLogoutResponseAdapter.adapt(logoutResponseDTO));
	}

}
