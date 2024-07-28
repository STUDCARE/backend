package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.UserRegisterResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserRegisterResponseAdapter.class})
class UserRegisterResponseAdapterTest {

	@Autowired
	private UserRegisterResponseAdapter userRegisterResponseAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		UserRegisterResponseDTO registerResponseDTO = new UserRegisterResponseDTO();
		registerResponseDTO.setResponseCode(Status.SUCCESS);
		registerResponseDTO.setMessage("User account created successfully for user@gmail.com");
		when(objectMapper.writeValueAsString(registerResponseDTO)).thenReturn("{\"responseCode\":\"SUCCESS\",\"message\":\"User account created successfully for user@gmail.com\"}");

		HttpResponseData responseData = userRegisterResponseAdapter.adapt(registerResponseDTO);

		assertEquals(HttpStatus.OK, responseData.getHttpStatus());
		assertEquals("{\"responseCode\":\"SUCCESS\",\"message\":\"User account created successfully for user@gmail.com\"}", responseData.getResponseBody());
	}

	@Test
	void adaptFailure() throws JsonProcessingException {
		UserRegisterResponseDTO registerResponseDTO = new UserRegisterResponseDTO();
		registerResponseDTO.setResponseCode(Status.FAILURE);
		registerResponseDTO.setMessage("User already exists with email: user@gmail.com");

		when(objectMapper.writeValueAsString(registerResponseDTO)).thenReturn("{\"responseCode\":\"FAILURE\",\"message\":\"User already exists with email: user@gmail.com\"}");

		HttpResponseData responseData = userRegisterResponseAdapter.adapt(registerResponseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, responseData.getHttpStatus());
		assertEquals("{\"responseCode\":\"FAILURE\",\"message\":\"User already exists with email: user@gmail.com\"}", responseData.getResponseBody());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		UserRegisterResponseDTO registerResponseDTO = new UserRegisterResponseDTO();
		registerResponseDTO.setResponseCode(Status.SUCCESS);

		when(objectMapper.writeValueAsString(registerResponseDTO)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> userRegisterResponseAdapter.adapt(registerResponseDTO));
	}
}
