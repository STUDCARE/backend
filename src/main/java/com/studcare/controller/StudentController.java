package com.studcare.controller;

import com.studcare.model.ClassTeacherNoteDTO;
import com.studcare.model.MonthlyEvaluationsDTO;
import com.studcare.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/{student}/term/results")
	public ResponseEntity<Object> getTermResults(
			@PathVariable String student,
			@RequestBody String requestBody) {
		try {
			log.info("StudentService.getTermResults()[GET] process initiated for student name: {}", student);
			return studentService.getStudentResults(student, requestBody);
		} catch (Exception exception) {
			log.error("StudentService.getTermResults()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{student}/year/results")
	public ResponseEntity<Object> getYearResults(
			@PathVariable String student,
			@RequestBody String requestBody) {
		try {
			log.info("StudentService.getYearResults()[GET] process initiated for student name: {}", student);
			return studentService.getStudentYearResults(student, requestBody);
		} catch (Exception exception) {
			log.error("StudentService.getYearResults()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{student}/all/results")
	public ResponseEntity<Object> getAllResults(@PathVariable String student) {
		try {
			log.info("StudentService.getAllResults()[GET] process initiated for student name: {}", student);
			return studentService.getStudentAllResults(student);
		} catch (Exception exception) {
			log.error("StudentService.getAllResults()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{studentId}/evaluations")
	public ResponseEntity<Object> getMonthlyEvaluations(
			@PathVariable String studentId,
			@RequestBody MonthlyEvaluationsDTO requestDTO) {
		try {
			log.info("StudentService.getMonthlyEvaluations()[POST] process initiated for student ID: {}", studentId);
			return studentService.getMonthlyEvaluations( studentId, requestDTO);
		} catch (Exception exception) {
			log.error("StudentService.getMonthlyEvaluations()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/term/note")
	public ResponseEntity<Object> addClassTeacherNote(@RequestBody ClassTeacherNoteDTO noteDTO) {
		try {
			log.info("StudentService.addClassTeacherNote()[POST] process initiated");
			return studentService.addClassTeacherNote(noteDTO);
		} catch (Exception exception) {
			log.error("StudentService.addClassTeacherNote()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{student}/suggestions")
	public ResponseEntity<Object> getStudentSuggestions(
			@PathVariable String student) {
		try {
			log.info("StudentService.getStudentSuggestions()[GET] process initiated for student name: {}", student);
			return studentService.getStudentSuggestions(student);
		} catch (Exception exception) {
			log.error("StudentService.getStudentSuggestions()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}