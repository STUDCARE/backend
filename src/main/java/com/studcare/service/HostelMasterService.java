package com.studcare.service;

import com.studcare.adapter.ResponseAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.MonthlyEvaluationAdapter;
import com.studcare.data.jpa.adaptor.StudentAdapter;
import com.studcare.data.jpa.adaptor.WardAdapter;
import com.studcare.data.jpa.entity.MonthlyEvaluation;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.entity.Ward;
import com.studcare.data.jpa.repository.MonthlyEvaluationRepository;
import com.studcare.data.jpa.repository.StudentRepository;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.data.jpa.repository.WardRepository;
import com.studcare.exception.StudCareDataException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.MonthlyEvaluationRequestDTO;
import com.studcare.model.ResponseDTO;
import com.studcare.model.WardDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j
@Service
public class HostelMasterService {
	@Autowired
	private WardRepository wardRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private MonthlyEvaluationRepository monthlyEvaluationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ResponseAdapter responseAdapter;
	@Autowired
	private WardAdapter wardAdapter;
	@Autowired
	private StudentAdapter studentAdapter;
	@Autowired
	private MonthlyEvaluationAdapter monthlyEvaluationAdapter;

	public ResponseEntity<Object> getWardDetails(String hostelMasterEmail) {
		log.info("HostelMasterService.getWardDetails() initiated for hostel master ID: {}", hostelMasterEmail);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			User hostelMaster = userRepository.findByEmail(hostelMasterEmail).orElseThrow(() -> new StudCareValidationException("Hostel Master not found"));
			Ward ward = wardRepository.findByHostelMaster(hostelMaster).orElseThrow(() -> new StudCareValidationException("Ward not found for this Hostel Master"));
			List<Student> students = studentRepository.findByWard(ward);
			WardDetailsDTO wardDetailsDTO = new WardDetailsDTO();
			wardDetailsDTO.setWardDTO(wardAdapter.adapt(ward));
			wardDetailsDTO.setStudents(students.stream().map(studentAdapter::adapt).collect(Collectors.toList()));
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Ward details retrieved successfully");
			responseDTO.setData(Collections.singletonList(wardDetailsDTO));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("HostelMasterService.getWardDetails() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("HostelMasterService.getWardDetails() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> addMonthlyEvaluation(String hostelMasterEmail, String studentId, MonthlyEvaluationRequestDTO evaluationRequestDTO) {
		log.info("HostelMasterService.addMonthlyEvaluation() initiated for hostel master ID: {} and student ID: {}", hostelMasterEmail, studentId);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			User hostelMaster = userRepository.findByEmail(hostelMasterEmail).orElseThrow(() -> new StudCareDataException("Hostel Master not found"));
			Student student = studentRepository.findByUser_Email(studentId).orElseThrow(() -> new StudCareDataException("Student not found"));
			Ward ward = wardRepository.findByWardName(evaluationRequestDTO.getWardName()).orElseThrow(() -> new StudCareDataException("Ward not found"));
			if (!ward.getHostelMaster().equals(hostelMaster)) {
				throw new StudCareValidationException("Hostel Master is not assigned to this ward");
			}
			if (ObjectUtils.isEmpty(student.getWard())){
				throw new StudCareValidationException("Student is not assigned to a ward");
			}
			else if (!student.getWard().equals(ward)) {
				throw new StudCareValidationException("Student is not assigned to this ward");
			}
			MonthlyEvaluation monthlyEvaluation = monthlyEvaluationAdapter.adapt(evaluationRequestDTO);
			monthlyEvaluation.setStudent(student);
			monthlyEvaluation.setHostelMasterId(hostelMaster);
			monthlyEvaluation.setWardId(ward);
			monthlyEvaluationRepository.save(monthlyEvaluation);
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Monthly evaluation added successfully");
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("HostelMasterService.addMonthlyEvaluation() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("HostelMasterService.addMonthlyEvaluation() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}
}