package com.studcare.model;

import com.studcare.data.jpa.entity.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
	private Long userId;
	private String email;
	private String password;
	private UserRole role;
	private	String username;
	private boolean classTeacher;
	private LocalDateTime createdTimestamp;
	private LocalDateTime modifiedTimestamp;
}
