package com.studcare.controller;

import com.studcare.service.UserService;
import com.studcare.model.HttpRequestData;
import com.studcare.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	@PostMapping("/logout/{email}")
	public ResponseEntity<Object> logoutUser(
			@PathVariable String email,
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams
	) {
		try {
			log.info("AccountController.logoutUser()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(email, headers, queryParams);
			return accountService.userLogout(httpRequestData);
		} catch (Exception exception) {
			log.error("AccountController.logoutUser()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/profile/{email}")
	public ResponseEntity<Object> viewUserProfile(
			@PathVariable String email,
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams
	) {
		try {
			log.info("AccountController.viewUserProfile()[GET] process initiated for userId: {}", email);
			HttpRequestData httpRequestData = new HttpRequestData(email, headers, queryParams);
			return accountService.viewUserProfile(httpRequestData);
		} catch (Exception exception) {
			log.error("AccountController.viewUserProfile()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllUsersByRole(
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody
	) {
		try {
			log.info("AccountController.getAllUsersByRole()[GET] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData( headers, queryParams, requestBody);
			return userService.getAllUsersByRole(httpRequestData);
		} catch (Exception exception) {
			log.error("AccountController.getAllUsersByRole()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
