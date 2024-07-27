package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.AddSubjectsToClassRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.model.SubjectsDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddSubjectsToClassRequestAdapter implements GenericRequestAdapter<HttpRequestData, AddSubjectsToClassRequestDTO> {
	@Autowired private ObjectMapper objectMapper;

	@Override public AddSubjectsToClassRequestDTO adapt(HttpRequestData httpRequestData) {
		AddSubjectsToClassRequestDTO subjectsToClassRequestDTO = new AddSubjectsToClassRequestDTO();
		subjectsToClassRequestDTO.setClassName(httpRequestData.getReference());
		subjectsToClassRequestDTO.setSubjects(mapSubjects(httpRequestData.getRequestBody()));
		return subjectsToClassRequestDTO;
	}

	private SubjectsDTO mapSubjects(String requestBody) {
		try {
			return objectMapper.readValue(requestBody, SubjectsDTO.class);
		} catch (JsonProcessingException exception) {
			log.error("AddStudentRequestAdapter.mapSubjects(): map user register request to user object failed", exception);
			throw new StudCareRuntimeException("request mapping failed");
		}
	}
}