package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.AddSubjectsToClassRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.model.SubjectsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AddSubjectsToClassRequestAdapter.class})
class AddSubjectsToClassRequestAdapterTest {

	@Autowired
	private AddSubjectsToClassRequestAdapter addSubjectsToClassRequestAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setReference("Class 10");
		String requestBody = "{\"subject\":\"Mathematics\"}";
		httpRequestData.setRequestBody(requestBody);

		SubjectsDTO subjectsDTO = new SubjectsDTO();
		subjectsDTO.setSubjects(Collections.singletonList("Mathematics"));

		when(objectMapper.readValue(requestBody, SubjectsDTO.class)).thenReturn(subjectsDTO);

		AddSubjectsToClassRequestDTO addSubjectsToClassRequestDTO = addSubjectsToClassRequestAdapter.adapt(httpRequestData);

		assertEquals("Class 10", addSubjectsToClassRequestDTO.getClassName());
		assertEquals(Collections.singletonList("Mathematics"), addSubjectsToClassRequestDTO.getSubjects().getSubjects());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setRequestBody("invalid-json");

		when(objectMapper.readValue("invalid-json", SubjectsDTO.class)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> addSubjectsToClassRequestAdapter.adapt(httpRequestData));
	}
}
