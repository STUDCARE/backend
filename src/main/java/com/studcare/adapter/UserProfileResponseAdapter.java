package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.UserProfileResponseDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserProfileResponseAdapter implements GenericRequestAdapter<UserProfileResponseDTO, HttpResponseData> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override public HttpResponseData adapt(UserProfileResponseDTO userProfileResponseDTO) {
		HttpResponseData responseData = new HttpResponseData();
		if (userProfileResponseDTO.getResponseCode().equals(Status.SUCCESS)) {
			responseData.setHttpStatus(HttpStatus.OK);
		} else {
			responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
		}
		responseData.setResponseBody(mapResponseData(userProfileResponseDTO));
		return responseData;
	}

	private String mapResponseData(UserProfileResponseDTO userProfileResponseDTO) {
		try {
			return objectMapper.writeValueAsString(userProfileResponseDTO);
		} catch (JsonProcessingException exception) {
			log.error("UserProfileResponseAdapter.mapResponseData(): map user profile response to response object failed", exception);
			throw new StudCareRuntimeException("user profile response to response object failed");
		}
	}
}