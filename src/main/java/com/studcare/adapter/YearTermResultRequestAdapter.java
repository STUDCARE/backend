package com.studcare.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.model.YearTermDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class YearTermResultRequestAdapter  {
	@Autowired private ObjectMapper objectMapper;

	 public YearTermDTO adapt(String requestBody) {
		 try {
			 return objectMapper.readValue(requestBody, YearTermDTO.class);
		 } catch (JsonProcessingException exception) {
			 log.error("YearTermResultRequestAdapter.adapt(): map object failed", exception);
			 throw new StudCareRuntimeException("request to  object failed");
		 }
	}
}