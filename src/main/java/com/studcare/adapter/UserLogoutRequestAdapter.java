package com.studcare.adapter;

import com.studcare.model.HttpRequestData;
import com.studcare.model.LogoutRequestDTO;
import com.studcare.template.GenericRequestAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserLogoutRequestAdapter implements GenericRequestAdapter<HttpRequestData, LogoutRequestDTO> {
	@Override
	public LogoutRequestDTO adapt(HttpRequestData httpRequestData) {
		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setHeaders(httpRequestData.getHeaders());
		logoutRequestDTO.setQueryParams(httpRequestData.getQueryParams());
		logoutRequestDTO.setEmail(httpRequestData.getReference());
		return logoutRequestDTO;
	}
}