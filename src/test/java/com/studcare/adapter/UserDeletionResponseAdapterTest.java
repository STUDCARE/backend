package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.UserDeletionResponseDTO;
import com.studcare.model.UserProfileResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserDeletionResponseAdapter.class})
class UserDeletionResponseAdapterTest {

	@Autowired
	private UserDeletionResponseAdapter userDeletionResponseAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		UserDeletionResponseDTO deletionResponseDTO = new UserDeletionResponseDTO();
		deletionResponseDTO.setResponseCode(Status.SUCCESS);
		String responseBody = "{\"response\":\"success\"}";

		when(objectMapper.writeValueAsString(deletionResponseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = userDeletionResponseAdapter.adapt(deletionResponseDTO);

		assertEquals(HttpStatus.OK, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptFailure() throws JsonProcessingException {
		UserDeletionResponseDTO deletionResponseDTO = new UserDeletionResponseDTO();
		deletionResponseDTO.setResponseCode(Status.FAILURE);
		String responseBody = "{\"response\":\"failure\"}";

		when(objectMapper.writeValueAsString(deletionResponseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = userDeletionResponseAdapter.adapt(deletionResponseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		UserDeletionResponseDTO deletionResponseDTO = new UserDeletionResponseDTO();
		deletionResponseDTO.setResponseCode(Status.SUCCESS);

		when(objectMapper.writeValueAsString(deletionResponseDTO)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> userDeletionResponseAdapter.adapt(deletionResponseDTO));
	}

}
