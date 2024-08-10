package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.AddTeacherToSubjectRequestDTO;
import com.studcare.model.HttpRequestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AddTeacherToSubjectRequestAdapter.class})
class AddTeacherToSubjectRequestAdapterTest {

	@Autowired
	private AddTeacherToSubjectRequestAdapter addTeacherToSubjectRequestAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		String requestBody = "{\"teacherId\":\"1234\", \"subjectId\":\"5678\"}";
		httpRequestData.setRequestBody(requestBody);

		AddTeacherToSubjectRequestDTO addTeacherToSubjectRequestDTO = new AddTeacherToSubjectRequestDTO();
		addTeacherToSubjectRequestDTO.setTeacher("1234");
		addTeacherToSubjectRequestDTO.setSubject("5678");

		when(objectMapper.readValue(requestBody, AddTeacherToSubjectRequestDTO.class)).thenReturn(addTeacherToSubjectRequestDTO);

		AddTeacherToSubjectRequestDTO result = addTeacherToSubjectRequestAdapter.adapt(httpRequestData);

		assertEquals("1234", result.getTeacher());
		assertEquals("5678", result.getSubject());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		String invalidJson = "invalid-json";
		httpRequestData.setRequestBody(invalidJson);

		when(objectMapper.readValue(invalidJson, AddTeacherToSubjectRequestDTO.class)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> addTeacherToSubjectRequestAdapter.adapt(httpRequestData));
	}
}
