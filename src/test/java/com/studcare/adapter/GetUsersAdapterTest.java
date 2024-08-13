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

import java.util.HashMap;
import java.util.Map;

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
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("role","admin");
		httpRequestData.setQueryParams(queryParams);

		AllUsersRequestDTO allUsersRequestDTO = new AllUsersRequestDTO();
		allUsersRequestDTO.setUserRole("admin");

		AllUsersRequestDTO result = getUsersAdapter.adapt(httpRequestData);

		assertEquals("admin", result.getUserRole());
	}
}
