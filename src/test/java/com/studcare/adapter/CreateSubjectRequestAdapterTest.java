package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.CreateSubjectRequestDTO;
import com.studcare.model.HttpRequestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CreateSubjectRequestAdapter.class})
class CreateSubjectRequestAdapterTest {

	@Autowired
	private CreateSubjectRequestAdapter createSubjectRequestAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		String requestBody = "{\"subjectName\":\"Mathematics\"}";
		httpRequestData.setRequestBody(requestBody);

		CreateSubjectRequestDTO createSubjectRequestDTO = new CreateSubjectRequestDTO();
		createSubjectRequestDTO.setSubjectName("Mathematics");

		when(objectMapper.readValue(requestBody, CreateSubjectRequestDTO.class)).thenReturn(createSubjectRequestDTO);

		CreateSubjectRequestDTO result = createSubjectRequestAdapter.adapt(httpRequestData);

		assertEquals("Mathematics", result.getSubjectName());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setRequestBody("invalid-json");

		when(objectMapper.readValue("invalid-json", CreateSubjectRequestDTO.class)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> createSubjectRequestAdapter.adapt(httpRequestData));
	}
}
