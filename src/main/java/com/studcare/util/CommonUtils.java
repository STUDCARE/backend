package com.studcare.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.studcare.model.HttpResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.StringJoiner;

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

	public static String appendString(String... values) {
		StringBuilder builder = new StringBuilder();
		for (String value : values) {
			builder.append(value);
		}
		return builder.toString();
	}

	public static String joinString(String delimiter, String... values) {
		StringJoiner joiner = new StringJoiner(delimiter);
		for (String value : values) {
			joiner.add(value);
		}
		return joiner.toString();
	}

	public static <T> String toJson(T object) {
		String message = "";
		try {
			Gson gson = new Gson();
			message = gson.toJson(object);
		} catch (JsonSyntaxException exception) {
			log.error("CommonUtils.toJson() an error occurred while processing the request", exception);
		}
		return message;
	}

	public static <T> T fromJson(String message, Class<T> type) {
		T object = null;
		try {
			Gson gson = new Gson();
			object = gson.fromJson(message, type);
		} catch (Exception exception) {
			log.error("CommonUtils.fromJson() an error occurred while processing the request", exception);
		}
		return object;
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
