package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ResponseAdapter.class})
class ResponseAdapterTest {

	@Autowired
	private ResponseAdapter responseAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);

		String responseBody = "{\"responseCode\":\"SUCCESS\"}";

		when(objectMapper.writeValueAsString(responseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = responseAdapter.adapt(responseDTO);

		assertEquals(HttpStatus.OK, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptFailure() throws JsonProcessingException {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.FAILURE);

		String responseBody = "{\"responseCode\":\"FAILURE\"}";

		when(objectMapper.writeValueAsString(responseDTO)).thenReturn(responseBody);

		HttpResponseData httpResponseData = responseAdapter.adapt(responseDTO);

		assertEquals(HttpStatus.BAD_REQUEST, httpResponseData.getHttpStatus());
		assertEquals(responseBody, httpResponseData.getResponseBody());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);

		when(objectMapper.writeValueAsString(responseDTO)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> responseAdapter.adapt(responseDTO));
	}

}
