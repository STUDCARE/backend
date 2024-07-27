package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpRequestData;
import com.studcare.model.ClassRequestDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClassRequestAdapter implements GenericRequestAdapter<HttpRequestData, ClassRequestDTO> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override public ClassRequestDTO adapt(HttpRequestData httpRequestData) {
		ClassRequestDTO userRegisterRequestDTO = new ClassRequestDTO();
		userRegisterRequestDTO.setHeaders(httpRequestData.getHeaders());
		userRegisterRequestDTO.setQueryParams(httpRequestData.getQueryParams());
		userRegisterRequestDTO.setSchoolClassDTO(mapUserData(httpRequestData.getRequestBody()));
		return userRegisterRequestDTO;
	}

	private SchoolClassDTO mapUserData(String requestBody) {
		try {
			return objectMapper.readValue(requestBody, SchoolClassDTO.class);
		} catch (JsonProcessingException exception) {
			log.error("ClassRequestAdapter.mapUserData(): map user register request to user object failed", exception);
			throw new StudCareRuntimeException("user register request to user object failed");
		}
	}
}
