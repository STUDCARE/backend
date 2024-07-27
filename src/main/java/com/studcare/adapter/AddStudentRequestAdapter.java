package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.AddStudentsDTO;
import com.studcare.model.AddStudentsRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddStudentRequestAdapter implements GenericRequestAdapter<HttpRequestData, AddStudentsRequestDTO> {
	@Autowired private ObjectMapper objectMapper;

	@Override
	public AddStudentsRequestDTO adapt(HttpRequestData httpRequestData) {
		AddStudentsRequestDTO addStudentsRequestDTO = new AddStudentsRequestDTO();
		addStudentsRequestDTO.setHeaders(httpRequestData.getHeaders());
		addStudentsRequestDTO.setQueryParams(httpRequestData.getQueryParams());
		addStudentsRequestDTO.setClassName(httpRequestData.getReference());
		addStudentsRequestDTO.setStudents(mapStudentEmails(httpRequestData.getRequestBody()));
		return addStudentsRequestDTO;
	}

	private AddStudentsDTO mapStudentEmails(String requestBody) {
		try {
			return objectMapper.readValue(requestBody, AddStudentsDTO.class);
		} catch (JsonProcessingException exception) {
			log.error("AddStudentRequestAdapter.mapStudentEmails(): map user register request to user object failed", exception);
			throw new StudCareRuntimeException("user register request to user object failed");
		}
	}
}