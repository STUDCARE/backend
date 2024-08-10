package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.UserDeletionResponseDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDeletionResponseAdapter implements GenericRequestAdapter<UserDeletionResponseDTO, HttpResponseData> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override public HttpResponseData adapt(UserDeletionResponseDTO deletionResponseDTO) {
		HttpResponseData responseData = new HttpResponseData();
		if (deletionResponseDTO.getResponseCode().equals(Status.SUCCESS)) {
			responseData.setHttpStatus(HttpStatus.OK);
		} else {
			responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
		}
		responseData.setResponseBody(mapResponseData(deletionResponseDTO));
		return responseData;
	}

	private String mapResponseData(UserDeletionResponseDTO deletionResponseDTO) {
		try {
			return objectMapper.writeValueAsString(deletionResponseDTO);
		} catch (JsonProcessingException exception) {
			log.error("UserRegisterResponseAdapter.mapResponseData(): map user deletion response to response object failed", exception);
			throw new StudCareRuntimeException("user deletion request to user object failed");
		}
	}
}