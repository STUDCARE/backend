package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.AddStudentsDTO;
import com.studcare.model.AddStudentsRequestDTO;
import com.studcare.model.HttpRequestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AddStudentRequestAdapter.class})
class AddStudentRequestAdapterTest {

	@Autowired
	private AddStudentRequestAdapter addStudentRequestAdapter;

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

		String reference = "ClassA";
		httpRequestData.setReference(reference);

		String requestBody = "{\"emails\":[\"student1@gmail.com\",\"student2@gmail.com\"]}";
		httpRequestData.setRequestBody(requestBody);

		AddStudentsDTO addStudentsDTO = new AddStudentsDTO();
		addStudentsDTO.setStudentEmails(Arrays.asList("student1@gmail.com", "student2@gmail.com"));

		when(objectMapper.readValue(requestBody, AddStudentsDTO.class)).thenReturn(addStudentsDTO);

		AddStudentsRequestDTO addStudentsRequestDTO = addStudentRequestAdapter.adapt(httpRequestData);

		assertEquals(headers, addStudentsRequestDTO.getHeaders());
		assertEquals(queryParams, addStudentsRequestDTO.getQueryParams());
		assertEquals(reference, addStudentsRequestDTO.getClassName());
		assertEquals(addStudentsDTO.getStudentEmails(), addStudentsRequestDTO.getStudents().getStudentEmails());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setRequestBody("invalid-json");

		when(objectMapper.readValue("invalid-json", AddStudentsDTO.class)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> addStudentRequestAdapter.adapt(httpRequestData));
	}
}
