package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.LogoutResponseDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserLogoutResponseAdapter implements GenericRequestAdapter<LogoutResponseDTO, HttpResponseData> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public HttpResponseData adapt(LogoutResponseDTO logoutResponseDTO) {
		HttpResponseData responseData = new HttpResponseData();
		if (logoutResponseDTO.getResponseCode().equals(Status.SUCCESS)) {
			responseData.setHttpStatus(HttpStatus.OK);
		} else{
			responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
		}
		responseData.setResponseBody(mapResponseData(logoutResponseDTO));
		return responseData;
	}

	private String mapResponseData(LogoutResponseDTO logoutResponseDTO) {
		try {
			return objectMapper.writeValueAsString(logoutResponseDTO);
		} catch (JsonProcessingException exception) {
			log.error("LogoutResponseAdapter.mapResponseData(): map logout response to response object failed", exception);
			throw new StudCareRuntimeException("logout response to response object failed");
		}
	}
}