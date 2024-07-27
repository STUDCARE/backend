package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.constants.Status;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResponseAdapter implements GenericRequestAdapter<ResponseDTO, HttpResponseData> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public HttpResponseData adapt(ResponseDTO classResponseDTO) {
		HttpResponseData responseData = new HttpResponseData();
		if (classResponseDTO.getResponseCode().equals(Status.SUCCESS)) {
			responseData.setHttpStatus(HttpStatus.OK);
		} else if (classResponseDTO.getResponseCode().equals(Status.FAILURE)) {
			responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
		}
		responseData.setResponseBody(mapResponseData(classResponseDTO));
		return responseData;
	}

	private String mapResponseData(ResponseDTO classResponseDTO) {
		try {
			return objectMapper.writeValueAsString(classResponseDTO);
		} catch (JsonProcessingException exception) {
			log.error("ClassResponseAdapter.mapResponseData(): map user class response to response object failed", exception);
			throw new StudCareRuntimeException("user class request to user object failed");
		}
	}
}