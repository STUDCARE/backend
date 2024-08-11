package com.studcare.security.auth.dto;

import com.studcare.data.jpa.entity.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
	private String email;
	private String password;
	private UserRole role;
	private	String username;
	private boolean isClassTeacher;
}
