package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.AllUsersRequestDTO;
import com.studcare.model.HttpRequestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GetUsersAdapter.class})
class GetUsersAdapterTest {

	@Autowired
	private GetUsersAdapter getUsersAdapter;

	@MockBean
	private ObjectMapper objectMapper;

	@Test
	void adaptSuccess() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		String requestBody = "{\"role\":\"admin\"}";
		httpRequestData.setRequestBody(requestBody);

		AllUsersRequestDTO allUsersRequestDTO = new AllUsersRequestDTO();
		allUsersRequestDTO.setUserRole("admin");

		when(objectMapper.readValue(requestBody, AllUsersRequestDTO.class)).thenReturn(allUsersRequestDTO);

		AllUsersRequestDTO result = getUsersAdapter.adapt(httpRequestData);

		assertEquals("admin", result.getUserRole());
	}

	@Test
	void adaptJsonProcessingException() throws JsonProcessingException {
		HttpRequestData httpRequestData = new HttpRequestData();
		httpRequestData.setRequestBody("invalid-json");

		when(objectMapper.readValue("invalid-json", AllUsersRequestDTO.class)).thenThrow(new JsonProcessingException("error") {});

		assertThrows(StudCareRuntimeException.class, () -> getUsersAdapter.adapt(httpRequestData));
	}
}
