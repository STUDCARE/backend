package com.studcare.adapter;

import com.studcare.model.HttpRequestData;
import com.studcare.model.UserDeletionRequestDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDeletionRequestAdapter implements GenericRequestAdapter<HttpRequestData, UserDeletionRequestDTO> {

	@Override
	public UserDeletionRequestDTO adapt(HttpRequestData httpRequestData) {
		UserDeletionRequestDTO userDeletionRequestDTO = new UserDeletionRequestDTO();
		userDeletionRequestDTO.setHeaders(httpRequestData.getHeaders());
		userDeletionRequestDTO.setQueryParams(httpRequestData.getQueryParams());
		userDeletionRequestDTO.setUserEmail(httpRequestData.getReference());
		return userDeletionRequestDTO;
	}
}