package com.studcare.model;

import lombok.Data;

import java.util.List;

@Data
public class AddStudentsDTO {
	private List<String> studentEmails;
}
