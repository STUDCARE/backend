package com.studcare.model;

import lombok.Data;

@Data
public class AddSubjectsToClassRequestDTO {
	private SubjectsDTO subjects;
	private String className;
}
