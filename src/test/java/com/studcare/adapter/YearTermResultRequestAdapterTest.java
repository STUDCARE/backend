package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.YearTermDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {YearTermResultRequestAdapter.class})
class YearTermResultRequestAdapterTest {

	@Autowired
	private YearTermResultRequestAdapter yearTermResultRequestAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		String requestBody = "{\"year\":\"2024\",\"term\":\"Fall\"}";
		YearTermDTO yearTermDTO = new YearTermDTO();
		yearTermDTO.setAcademicYear("2024");
		yearTermDTO.setTerm("Fall");

		when(objectMapper.readValue(requestBody, YearTermDTO.class)).thenReturn(yearTermDTO);

		YearTermDTO result = yearTermResultRequestAdapter.adapt(requestBody);

		assertEquals("2024", result.getAcademicYear());
		assertEquals("Fall", result.getTerm());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		String requestBody = "invalid-json";

		when(objectMapper.readValue(requestBody, YearTermDTO.class))
				.thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> yearTermResultRequestAdapter.adapt(requestBody));
	}
}
