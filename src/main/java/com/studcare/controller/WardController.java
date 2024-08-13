package com.studcare.controller;

import com.studcare.model.HttpRequestData;
import com.studcare.service.WardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ward")
public class WardController {

	@Autowired
	private WardService wardService;

	@PostMapping("/create")
	public ResponseEntity<Object> createWard(
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody) {
		try {
			log.info("WardController.createWard()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(headers, queryParams, requestBody);
			return wardService.createWard(httpRequestData);
		} catch (Exception exception) {
			log.error("WardController.createWard()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{wardName}/add/students")
	public ResponseEntity<Object> addStudents(
			@PathVariable String wardName,
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody) {
		try {
			log.info("WardController.addStudents()[POST] process initiated for ward ID: {}", wardName);
			HttpRequestData httpRequestData = new HttpRequestData(wardName, headers, queryParams, requestBody);
			return wardService.addStudents(wardName, httpRequestData);
		} catch (Exception exception) {
			log.error("WardController.addStudents()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{wardName}/students")
	public ResponseEntity<Object> getStudentsInWard(@PathVariable String wardName) {
		try {
			log.info("WardController.getStudentsInWard()[GET] process initiated for ward ID: {}", wardName);
			return wardService.getStudentsInWard(wardName);
		} catch (Exception exception) {
			log.error("WardController.getStudentsInWard()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{wardName}/details")
	public ResponseEntity<Object> getWardDetails(@PathVariable String wardName) {
		try {
			log.info("WardController.getWardDetails()[GET] process initiated for ward ID: {}", wardName);
			return wardService.getWardDetails(wardName);
		} catch (Exception exception) {
			log.error("WardController.getWardDetails()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllWards() {
		try {
			log.info("WardController.getAllWards()[GET] process initiated");
			return wardService.getAllWards();
		} catch (Exception exception) {
			log.error("WardController.getAllWards()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}