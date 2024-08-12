package com.studcare.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class HttpRequestData {
	private String reference;
	private Map<String, String> headers;
	private Map<String, String> queryParams;
	private String requestBody;

	public HttpRequestData(String reference, Map<String, String> headers, Map<String, String> queryParams, String requestBody) {
		this.reference = reference;
		this.headers = headers;
		this.queryParams = queryParams;
		this.requestBody = requestBody;
	}

	public HttpRequestData(Map<String, String> headers, Map<String, String> queryParams, String requestBody) {
		this.headers = headers;
		this.requestBody = requestBody;
		this.queryParams = queryParams;
	}

	public HttpRequestData(String userId, Map<String, String> headers, Map<String, String> queryParams) {
		this.reference = userId;
		this.headers = headers;
		this.queryParams = queryParams;
	}

	public HttpRequestData(Map<String, String> headers, Map<String, String> queryParams) {
		this.headers = headers;
		this.queryParams = queryParams;
	}
}