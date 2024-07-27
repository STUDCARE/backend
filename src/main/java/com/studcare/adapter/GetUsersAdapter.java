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
		try {
			return objectMapper.readValue(httpRequestData.getRequestBody(), AllUsersRequestDTO.class);
		} catch (JsonProcessingException exception) {
			log.error("GetUsersAdapter.adapt(): map user register request to user object failed", exception);
			throw new StudCareRuntimeException("get role from request body object failed");
		}
	}
}
