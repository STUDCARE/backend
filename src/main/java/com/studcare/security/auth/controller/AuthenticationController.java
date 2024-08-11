package com.studcare.security.auth.controller;

import com.studcare.model.HttpRequestData;
import com.studcare.security.auth.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<Object> authenticate(
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody
	) {
		try {
			log.info("AccountController.authenticate()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(headers, queryParams, requestBody);
			return authenticationService.authenticate(httpRequestData);
		} catch (Exception exception) {
			log.error("AccountController.authenticate()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
