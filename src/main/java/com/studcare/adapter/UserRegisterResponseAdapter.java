package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.UserRegisterResponseDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisterResponseAdapter implements GenericRequestAdapter<UserRegisterResponseDTO, HttpResponseData> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override public HttpResponseData adapt(UserRegisterResponseDTO registerResponseDTO) {
		HttpResponseData responseData = new HttpResponseData();
		if (registerResponseDTO.getResponseCode().equals(Status.SUCCESS)) {
			responseData.setHttpStatus(HttpStatus.OK);
		} else {
			responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
		}
		responseData.setResponseBody(mapResponseData(registerResponseDTO));
		return responseData;
	}

	private String mapResponseData(UserRegisterResponseDTO registerResponseDTO) {
		try {
			return objectMapper.writeValueAsString(registerResponseDTO);
		} catch (JsonProcessingException exception) {
			log.error("UserRegisterResponseAdapter.mapResponseData(): map user register response to response object failed", exception);
			throw new StudCareRuntimeException("user register request to user object failed");
		}
	}
}