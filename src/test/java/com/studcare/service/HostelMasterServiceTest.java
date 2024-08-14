package com.studcare.service;

import com.studcare.adapter.ResponseAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.MonthlyEvaluationAdapter;
import com.studcare.data.jpa.adaptor.StudentAdapter;
import com.studcare.data.jpa.adaptor.WardAdapter;
import com.studcare.data.jpa.dto.StudentDTO;
import com.studcare.data.jpa.dto.WardDTO;
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
import com.studcare.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class HostelMasterServiceTest {

	@Autowired
	private HostelMasterService hostelMasterService;

	@MockBean
	private WardRepository wardRepository;

	@MockBean
	private StudentRepository studentRepository;

	@MockBean
	private MonthlyEvaluationRepository monthlyEvaluationRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private ResponseAdapter responseAdapter;

	@MockBean
	private WardAdapter wardAdapter;

	@MockBean
	private StudentAdapter studentAdapter;

	@MockBean
	private MonthlyEvaluationAdapter monthlyEvaluationAdapter;

	private User hostelMaster;
	private Ward ward;
	private Student student;
	private MonthlyEvaluation monthlyEvaluation;

	@BeforeEach
	public void setup() {
		hostelMaster = new User();
		hostelMaster.setEmail("hostelmaster@example.com");

		ward = new Ward();
		ward.setHostelMaster(hostelMaster);

		student = new Student();
		student.setWard(ward);

		monthlyEvaluation = new MonthlyEvaluation();
	}

//	@Test
//	void testGetWardDetails_Success() {
//		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(hostelMaster));
//		List<Ward> w\
//		when(wardRepository.findByHostelMaster(any(User.class))).thenReturn((ward);
//		when(studentRepository.findByWard(any(Ward.class))).thenReturn(List.of(student));
//		when(wardAdapter.adapt(any(Ward.class))).thenReturn(new WardDTO());
//		when(studentAdapter.adapt(any(Student.class))).thenReturn(new StudentDTO());
//
//		ResponseDTO responseDTO = new ResponseDTO();
//		responseDTO.setResponseCode(Status.SUCCESS);
//		responseDTO.setMessage("Ward details retrieved successfully");
//
//		HttpResponseData httpResponseData = new HttpResponseData();
//		httpResponseData.setHttpStatus(HttpStatus.OK);
//
//		when(responseAdapter.adapt(any(ResponseDTO.class))).thenReturn(httpResponseData);
//
//		ResponseEntity<Object> response = hostelMasterService.getWardDetails("hostelmaster@example.com");
//
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//	}

	@Test
	void testGetWardDetails_HostelMasterNotFound() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

		ResponseEntity<Object> response = hostelMasterService.getWardDetails("nonexistent@example.com");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

//	@Test
//	void testGetWardDetails_WardNotFound() {
//		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(hostelMaster));
//		when(wardRepository.findByHostelMaster(any(User.class))).thenReturn(Optional.empty());
//
//		ResponseEntity<Object> response = hostelMasterService.getWardDetails("hostelmaster@example.com");
//
//		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//	}

	@Test
	void testAddMonthlyEvaluation_Success() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(hostelMaster));
		when(studentRepository.findByUser_Email(anyString())).thenReturn(Optional.of(student));
		when(wardRepository.findByWardName(anyString())).thenReturn(Optional.of(ward));
		when(monthlyEvaluationAdapter.adapt(any(MonthlyEvaluationRequestDTO.class))).thenReturn(monthlyEvaluation);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		responseDTO.setMessage("Monthly evaluation added successfully");

		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);

		when(responseAdapter.adapt(any(ResponseDTO.class))).thenReturn(httpResponseData);

		MonthlyEvaluationRequestDTO requestDTO = new MonthlyEvaluationRequestDTO();
		requestDTO.setWardName("SomeWard");

		ResponseEntity<Object> response = hostelMasterService.addMonthlyEvaluation("hostelmaster@example.com", "student@example.com", requestDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testAddMonthlyEvaluation_HostelMasterNotFound() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

		MonthlyEvaluationRequestDTO requestDTO = new MonthlyEvaluationRequestDTO();
		ResponseEntity<Object> response = hostelMasterService.addMonthlyEvaluation("nonexistent@example.com", "student@example.com", requestDTO);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testAddMonthlyEvaluation_StudentNotFound() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(hostelMaster));
		when(studentRepository.findByUser_Email(anyString())).thenReturn(Optional.empty());

		MonthlyEvaluationRequestDTO requestDTO = new MonthlyEvaluationRequestDTO();
		ResponseEntity<Object> response = hostelMasterService.addMonthlyEvaluation("hostelmaster@example.com", "nonexistent@example.com", requestDTO);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testAddMonthlyEvaluation_WardNotFound() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(hostelMaster));
		when(studentRepository.findByUser_Email(anyString())).thenReturn(Optional.of(student));
		when(wardRepository.findByWardName(anyString())).thenReturn(Optional.empty());

		MonthlyEvaluationRequestDTO requestDTO = new MonthlyEvaluationRequestDTO();
		requestDTO.setWardName("NonexistentWard");

		ResponseEntity<Object> response = hostelMasterService.addMonthlyEvaluation("hostelmaster@example.com", "student@example.com", requestDTO);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

}
