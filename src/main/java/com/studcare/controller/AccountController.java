package com.studcare.controller;

import com.studcare.model.HttpRequestData;
import com.studcare.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private AccountService accountService;

	@PostMapping("/create")
	public ResponseEntity<Object> createUser(
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody
	) {
		try {
			log.info("AccountController.adaptCreateUser()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(headers, queryParams, requestBody);
			return accountService.createUser(httpRequestData);
		} catch (Exception exception) {
			log.error("NotificationController.processNotification()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/delete/{email}")
	public ResponseEntity<Object> deleteUser(
			@PathVariable String email,
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams
	) {
		try {
			log.info("AccountController.deleteUser()[POST] process initiated for gatewayIdentifier : {}", email);
			HttpRequestData httpRequestData = new HttpRequestData(email, headers, queryParams);
			return accountService.deleteUser(httpRequestData);
		} catch (Exception exception) {
			log.error("NotificationController.processNotification()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
