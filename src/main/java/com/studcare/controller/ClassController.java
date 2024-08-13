package com.studcare.controller;

import com.studcare.model.HttpRequestData;
import com.studcare.model.YearTermDTO;
import com.studcare.service.ClassService;
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
@RequestMapping("/class")
public class ClassController {
	@Autowired
	private ClassService classService;

	@PostMapping("/create")
	public ResponseEntity<Object> createClass(
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody) {
		try {
			log.info("ClassController.createClass()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(headers, queryParams, requestBody);
			return classService.createClass(httpRequestData);
		} catch (Exception exception) {
			log.error("ClassController.createClass()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{className}/add/students")
	public ResponseEntity<Object> addStudents(
			@PathVariable String className,
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody) {
		try {
			log.info("ClassController.addStudents()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(className, headers, queryParams, requestBody);
			return classService.addStudents(httpRequestData);
		} catch (Exception exception) {
			log.error("ClassController.addStudents()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{className}/add/subjects")
	public ResponseEntity<Object> addSubjects(
			@PathVariable String className,
			@RequestHeader Map<String, String> headers,
			@RequestParam Map<String, String> queryParams,
			@RequestBody String requestBody) {
		try {
			log.info("ClassController.addSubjects()[POST] process initiated");
			HttpRequestData httpRequestData = new HttpRequestData(className, headers, queryParams, requestBody);
			return classService.addSubjectsToClass(httpRequestData);
		} catch (Exception exception) {
			log.error("ClassController.addSubjects()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{className}/{subject}/assign/{teacher}")
	public ResponseEntity<Object> addTeacherToClassSubject(
			@PathVariable String teacher,
			@PathVariable String subject,
			@PathVariable String className) {
		try {
			log.info("ClassController.getClassesForTeacherAndSubject()[GET] process initiated for teacher ID: {} and subject ID: {}", teacher, subject);
			return classService.addTeacherToClassSubject(teacher, subject, className);
		} catch (Exception exception) {
			log.error("ClassController.getClassesForTeacherAndSubject()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{className}/students")
	public ResponseEntity<Object> getStudentsInClass(@PathVariable String className) {
		try {
			log.info("ClassController.getStudentsInClass()[GET] process initiated");
			return classService.getStudentsInClass(className);
		} catch (Exception exception) {
			log.error("ClassController.getStudentsInClass()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{teacherId}/{subjectId}")
	public ResponseEntity<Object> getClassesForTeacherAndSubject(
			@PathVariable String teacherId,
			@PathVariable String subjectId) {
		try {
			log.info("ClassController.getClassesForTeacherAndSubject()[GET] process initiated for teacher ID: {} and subject ID: {}", teacherId, subjectId);
			return classService.getClassesForTeacherAndSubject(teacherId, subjectId);
		} catch (Exception exception) {
			log.error("ClassController.getClassesForTeacherAndSubject()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{className}/details")
	public ResponseEntity<Object> getClassDetails(@PathVariable String className) {
		try {
			log.info("ClassController.getClassDetails()[GET] process initiated for class name: {}", className);
			return classService.getClassDetails(className);
		} catch (Exception exception) {
			log.error("ClassController.getClassDetails()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/{className}/results")
	public ResponseEntity<Object> getClassResults(
			@PathVariable String className,
			@RequestHeader Map<String, String> headers,
			@RequestBody String requestBody) {
		try {
			log.info("ClassController.getClassResults()[POST] process initiated for class name: {}", className);
			return classService.getClassResults(className, requestBody);
		} catch (Exception exception) {
			log.error("ClassController.getClassResults()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{className}/term/result")
	public ResponseEntity<Object> calculateClassResults(
			@PathVariable String className,
			@RequestBody YearTermDTO yearTermDTO) {
		try {
			log.info("ClassController.calculateClassResults()[POST] process initiated for class name: {} and academic year: {}, term: {}", className, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm());
			return classService.calculateClassResults(className, yearTermDTO);
		} catch (Exception exception) {
			log.error("ClassController.calculateClassResults()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllClasses() {
		try {
			log.info("ClassController.getAllClasses()[GET] process initiated");
			return classService.getAllClasses();
		} catch (Exception exception) {
			log.error("ClassController.getAllClasses()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}