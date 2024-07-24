package com.studcare.model;

import lombok.Data;

@Data
public class SubjectResultDTO {
	private Long subjectId;
	private String subjectName;
	private String marks;
	private String grade;
	private String teacherNote;
	private String teacherName;
}