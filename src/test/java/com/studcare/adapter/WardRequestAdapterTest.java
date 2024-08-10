package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.data.jpa.dto.WardDTO;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpRequestData;
import com.studcare.model.WardRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WardRequestAdapter.class})
class WardRequestAdapterTest {

	@Autowired
	private WardRequestAdapter wardRequestAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		String requestBody = "{\"wardName\":\"Test Ward\"}";
		httpRequestData.setRequestBody(requestBody);

		WardDTO wardDTO = new WardDTO();
		wardDTO.setWardName("Test Ward");

		when(objectMapper.readValue(requestBody, WardDTO.class)).thenReturn(wardDTO);

		WardRequestDTO wardRequestDTO = wardRequestAdapter.adapt(httpRequestData);

		assertEquals("Test Ward", wardRequestDTO.getWardDTO().getWardName());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setRequestBody("invalid-json");
		when(objectMapper.readValue("invalid-json", WardDTO.class)).thenThrow(new JsonProcessingException("error") {});
		assertThrows(StudCareRuntimeException.class, () -> wardRequestAdapter.adapt(httpRequestData));
	}
}
