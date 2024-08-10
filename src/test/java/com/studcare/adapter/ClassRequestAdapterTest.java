package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpRequestData;
import com.studcare.model.ClassRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ClassRequestAdapter.class})
class ClassRequestAdapterTest {

	@Autowired
	private ClassRequestAdapter classRequestAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application/json");
		httpRequestData.setHeaders(headers);
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("query-param", "test-param");
		httpRequestData.setQueryParams(queryParams);
		String requestBody = "{\"className\":\"Math 101\"}";
		httpRequestData.setRequestBody(requestBody);

		SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
		schoolClassDTO.setClassName("Math 101");

		when(objectMapper.readValue(requestBody, SchoolClassDTO.class)).thenReturn(schoolClassDTO);

		ClassRequestDTO classRequestDTO = classRequestAdapter.adapt(httpRequestData);

		assertEquals(headers, classRequestDTO.getHeaders());
		assertEquals(queryParams, classRequestDTO.getQueryParams());
		assertEquals("Math 101", classRequestDTO.getSchoolClassDTO().getClassName());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setRequestBody("invalid-json");
		when(objectMapper.readValue("invalid-json", SchoolClassDTO.class)).thenThrow(new JsonProcessingException("error") {});
		assertThrows(StudCareRuntimeException.class, () -> classRequestAdapter.adapt(httpRequestData));
	}
}
