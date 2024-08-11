package com.studcare.security.controller;

import com.studcare.security.auth.dto.AuthenticationRequest;
import com.studcare.security.auth.dto.AuthenticationResponse;
import com.studcare.security.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
	}

}
