package com.studcare.controller;

import com.studcare.model.HttpRequestData;
import com.studcare.service.SubjectService;
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
@RequestMapping("/subject")
public class SubjectController {
	@Autowired
	private SubjectService subjectService;

	@PostMapping("/create")
	public ResponseEntity<Object> createSubject(
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody) {
		try {
			log.info("SubjectController.createSubject()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(headers, queryParams, requestBody);
			return subjectService.createSubject(httpRequestData);
		} catch (Exception exception) {
			log.error("SubjectController.createSubject()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/add/teacher")
	public ResponseEntity<Object> addTeacher(
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody) {
		try {
			log.info("SubjectController.addTeacher()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(headers, queryParams, requestBody);
			return subjectService.addTeacher(httpRequestData);
		} catch (Exception exception) {
			log.error("SubjectController.addTeacher()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllSubjects() {
		try {
			log.info("SubjectController.getAllSubjects()[GET] process initiated");
			return subjectService.getAllSubjects();
		} catch (Exception exception) {
			log.error("SubjectController.getAllSubjects()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/teacher/{teacher}")
	public ResponseEntity<Object> getSubjectsForTeacher(@PathVariable String teacher) {
		try {
			log.info("SubjectController.getSubjectsForTeacher()[GET] process initiated");
			return subjectService.getSubjectsForTeacher(teacher);
		} catch (Exception exception) {
			log.error("SubjectController.getSubjectsForTeacher()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{subject}/teachers")
	public ResponseEntity<Object> getTeachersForSubject(@PathVariable String subject) {
		try {
			log.info("SubjectController.getTeachersForSubject()[GET] process initiated");
			return subjectService.getTeachersForSubject(subject);
		} catch (Exception exception) {
			log.error("SubjectController.getTeachersForSubject()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}