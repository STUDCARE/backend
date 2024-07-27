package com.studcare.model;

import lombok.Data;

import java.util.Map;

@Data
public class AddStudentsRequestDTO {
	private Map<String,String> headers;
	private Map<String,String> queryParams;
	private AddStudentsDTO students;
	private String className;
}
