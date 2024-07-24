package com.studcare.model;

import lombok.Data;

@Data
public class ClassTeacherNoteDTO {
	private String student;
	private String teacher;
	private String term;
	private String academicYear;
	private String classTeacherNote;
}