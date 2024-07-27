package com.studcare.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.CreateSubjectRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateSubjectRequestAdapter implements GenericRequestAdapter<HttpRequestData, CreateSubjectRequestDTO> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public CreateSubjectRequestDTO adapt(HttpRequestData httpRequestData) {
		try {
			return objectMapper.readValue(httpRequestData.getRequestBody(), CreateSubjectRequestDTO.class);
		} catch (Exception e) {
			log.error("CreateSubjectRequestAdapter.adapt(): Failed to map request", e);
			throw new StudCareRuntimeException("Request mapping failed");
		}
	}
}