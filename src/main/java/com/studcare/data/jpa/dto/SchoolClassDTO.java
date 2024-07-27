package com.studcare.data.jpa.dto;

import com.studcare.model.UserDTO;
import lombok.Data;

@Data
public class SchoolClassDTO {
	private String className;
	private UserDTO classTeacher;
}
