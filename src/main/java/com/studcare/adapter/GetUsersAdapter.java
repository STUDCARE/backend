package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.AllUsersRequestDTO;
import com.studcare.model.HttpRequestData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetUsersAdapter {
	@Autowired
	private ObjectMapper objectMapper;

	public AllUsersRequestDTO adapt(HttpRequestData httpRequestData) {
		AllUsersRequestDTO allUsersRequestDTO = new AllUsersRequestDTO();
		allUsersRequestDTO.setUserRole(httpRequestData.getQueryParams().get("role"));
		return allUsersRequestDTO;
	}
}
