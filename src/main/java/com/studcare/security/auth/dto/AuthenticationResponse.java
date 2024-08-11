package com.studcare.security.auth.dto;

import com.studcare.data.jpa.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
	private String token;
	private User user;

}
