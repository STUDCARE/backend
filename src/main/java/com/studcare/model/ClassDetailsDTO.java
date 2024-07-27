package com.studcare.model;

import com.studcare.data.jpa.dto.StudentDTO;
import com.studcare.data.jpa.dto.SubjectTeacherDTO;
import lombok.Data;

import java.util.List;

@Data
public class ClassDetailsDTO {
	private Long classId;
	private String className;
	private UserDTO classTeacher;
	private List<StudentDTO> students;
	private List<SubjectTeacherDTO> subjectTeachers;

}