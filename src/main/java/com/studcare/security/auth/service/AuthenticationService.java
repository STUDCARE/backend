package com.studcare.security.auth.service;

import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.security.JwtService;
import com.studcare.security.auth.dto.AuthenticationRequest;
import com.studcare.security.auth.dto.AuthenticationResponse;
import com.studcare.exception.StudCareValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;

	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new StudCareValidationException("User not found"));
		var jwtToken = jwtService.generateToken(user);
		log.info("successfully login {}",user);
		return AuthenticationResponse.builder().token(jwtToken).user(user).build();
	}
}