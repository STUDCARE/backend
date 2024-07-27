package com.studcare.controller;

import com.studcare.model.MonthlyEvaluationRequestDTO;
import com.studcare.service.HostelMasterService;
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
@RequestMapping("/hostelmaster")
public class HostelMasterController {

	@Autowired
	private HostelMasterService hostelMasterService;

	@GetMapping("/{hostelMasterId}/ward")
	public ResponseEntity<Object> getWardDetails(@PathVariable String hostelMasterId) {
		try {
			log.info("HostelMasterController.getWardDetails()[GET] process initiated for hostel master ID: {}", hostelMasterId);
			return hostelMasterService.getWardDetails(hostelMasterId);
		} catch (Exception exception) {
			log.error("HostelMasterController.getWardDetails()[GET] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{hostelMasterId}/student/{studentId}/evaluation")
	public ResponseEntity<Object> addMonthlyEvaluation(
			@PathVariable String hostelMasterId,
			@PathVariable String studentId,
			@RequestBody MonthlyEvaluationRequestDTO evaluationRequestDTO) {
		try {
			log.info("HostelMasterController.addMonthlyEvaluation()[POST] process initiated for hostel master ID: {} and student ID: {}", hostelMasterId, studentId);
			return hostelMasterService.addMonthlyEvaluation(hostelMasterId, studentId, evaluationRequestDTO);
		} catch (Exception exception) {
			log.error("HostelMasterController.addMonthlyEvaluation()[POST] unexpected error occurred", exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}