package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import com.studcare.model.UserProfileResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserProfileResponseAdapter.class})
class UserProfileResponseAdapterTest {

	@Autowired
	private UserProfileResponseAdapter userProfileResponseAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
		userProfileResponseDTO.setResponseCode(Status.SUCCESS);

		String responseBody = "{\"email\":\"user@gmail.com\"}";
		when(objectMapper.writeValueAsString(userProfileResponseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = userProfileResponseAdapter.adapt(userProfileResponseDTO);

		assertEquals(HttpStatus.OK, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptFailure() throws JsonProcessingException {
		UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
		userProfileResponseDTO.setResponseCode(Status.FAILURE);

		String responseBody = "{\"error\":\"something went wrong\"}";
		when(objectMapper.writeValueAsString(userProfileResponseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = userProfileResponseAdapter.adapt(userProfileResponseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
		userProfileResponseDTO.setResponseCode(Status.SUCCESS);

		when(objectMapper.writeValueAsString(userProfileResponseDTO)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> userProfileResponseAdapter.adapt(userProfileResponseDTO));
	}
}
