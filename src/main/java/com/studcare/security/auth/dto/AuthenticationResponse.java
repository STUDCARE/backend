package com.studcare.security.auth.dto;

import com.studcare.constants.Status;
import com.studcare.data.jpa.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
public class AuthenticationResponse {
	private String token;
	private User user;
	private Status responseCode;
	private String message;
}
