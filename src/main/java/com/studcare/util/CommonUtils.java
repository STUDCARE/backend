package com.studcare.util;

import com.studcare.model.HttpResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Slf4j
public class CommonUtils {
	private CommonUtils() {
		// to hide public constructor
	}
	public static boolean isEmpty(String... parameters) {
		for (String parameter : parameters) {
			if (ObjectUtils.isEmpty(parameter)) {
				return true;
			}
		}
		return false;
	}

	public static ResponseEntity<Object> createResponseEntity(HttpResponseData responseData) {
		HttpHeaders responseHeaders = new HttpHeaders();
		if(!ObjectUtils.isEmpty(responseData.getHeaders())) {
			for (Map.Entry<String, String> entry : responseData.getHeaders().entrySet()) {
				responseHeaders.add(entry.getKey(), entry.getValue());
			}
		}
		return new ResponseEntity<>(responseData.getResponseBody(), responseHeaders, responseData.getHttpStatus());
	}
}
