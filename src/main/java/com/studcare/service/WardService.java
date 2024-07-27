package com.studcare.service;

import com.studcare.adapter.AddStudentRequestAdapter;
import com.studcare.adapter.ResponseAdapter;
import com.studcare.adapter.WardRequestAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.SchoolClassAdapter;
import com.studcare.data.jpa.adaptor.UserAdapter;
import com.studcare.data.jpa.adaptor.WardAdapter;
import com.studcare.data.jpa.dto.StudentDTO;
import com.studcare.data.jpa.dto.WardDTO;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.entity.Ward;
import com.studcare.data.jpa.repository.StudentRepository;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.data.jpa.repository.WardRepository;
import com.studcare.exception.StudCareDataException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.AddStudentsRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import com.studcare.model.UserDTO;
import com.studcare.model.WardDetailsDTO;
import com.studcare.model.WardRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j
@Service
public class WardService {

	@Autowired
	private WardRepository wardRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private WardRequestAdapter wardRequestAdapter;
	@Autowired
	private AddStudentRequestAdapter addStudentRequestAdapter;
	@Autowired
	private ResponseAdapter responseAdapter;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserAdapter userAdapter;
	@Autowired
	private WardAdapter wardAdapter;
	@Autowired
	private SchoolClassAdapter schoolClassAdapter;


	public ResponseEntity<Object> createWard(HttpRequestData httpRequestData) {
		log.info("WardService.createWard() initiated");
		ResponseEntity<Object> responseEntity;
		ResponseDTO wardResponseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			WardRequestDTO wardRequestDTO = wardRequestAdapter.adapt(httpRequestData);
			wardResponseDTO = addWard(wardRequestDTO.getWardDTO());
			httpResponseData = responseAdapter.adapt(wardResponseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("WardService.createWard() finished for {}", wardRequestDTO.getWardDTO().getWardName());
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("WardService.createWard() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("WardService.createWard() an error occurred", exception);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> addStudents(String wardName, HttpRequestData httpRequestData) {
		log.info("WardService.addStudents() initiated");
		ResponseEntity<Object> responseEntity;
		ResponseDTO responseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			AddStudentsRequestDTO addStudentsRequestDTO = addStudentRequestAdapter.adapt(httpRequestData);
			responseDTO = addStudentsToWard(wardName, addStudentsRequestDTO.getStudents().getStudentEmails());
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("WardService.addStudents() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("WardService.addStudents() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("WardService.addStudents() an error occurred", exception);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getStudentsInWard(String wardName) {
		log.info("WardService.getStudentsInWard() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Ward ward = wardRepository.findByWardName(wardName).orElseThrow(() -> new StudCareValidationException("Ward not found"));
			List<Student> students = studentRepository.findByWard(ward);
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Students in ward retrieved successfully");
			responseDTO.setData(students);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("WardService.getStudentsInWard() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("WardService.getStudentsInWard() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("WardService.getStudentsInWard() an error occurred", exception);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getWardDetails(String wardName) {
		log.info("WardService.getWardDetails() initiated for ward ID: {}", wardName);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Ward ward = wardRepository.findByWardName(wardName).orElseThrow(() -> new StudCareValidationException("Ward not found"));
			List<Student> students = studentRepository.findByWard(ward);
			WardDetailsDTO wardDetails = new WardDetailsDTO();
			wardDetails.setWardName(ward.getWardName());
			wardDetails.setHostelMaster(userAdapter.adapt(ward.getHostelMaster()));
			wardDetails.setStudents(students.stream().map(this::adaptStudent).collect(Collectors.toList()));
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Ward details retrieved successfully");
			responseDTO.setData(Collections.singletonList(wardDetails));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("WardService.getWardDetails() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("WardService.getWardDetails() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	private ResponseDTO addWard(WardDTO wardDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		User hostelMaster = userRepository.findByEmail(wardDTO.getHostelMaster().getEmail()).orElseThrow(() -> new StudCareValidationException("Hostel master not found"));
		UserDTO hostelMasterDTO = userAdapter.adapt(hostelMaster);
		wardDTO.setHostelMaster(hostelMasterDTO);
		Ward ward = wardAdapter.adapt(wardDTO);
		if (wardRepository.findByWardName(ward.getWardName()).isPresent()) {
			responseDTO.setResponseCode(Status.FAILURE);
			responseDTO.setMessage("Ward already exists with name: " + ward.getWardName());
		} else {
			try {
				wardRepository.save(ward);
				responseDTO.setResponseCode(Status.SUCCESS);
				responseDTO.setMessage("Ward created successfully for " + ward.getWardName());
			} catch (Exception exception) {
				log.error("WardService.addWard error occurred ", exception);
				throw new StudCareDataException("Failed saving ward to the database");
			}
		}
		return responseDTO;
	}

	private ResponseDTO addStudentsToWard(String wardName, List<String> studentEmails) {
		ResponseDTO responseDTO = new ResponseDTO();
		Optional<Ward> optionalWard = wardRepository.findByWardName(wardName);
		if (optionalWard.isPresent()) {
			Ward ward = optionalWard.get();
			try {
				for (String studentEmail : studentEmails) {
					Optional<Student> optionalStudent = studentRepository.findByUser_Email(studentEmail);
					if (optionalStudent.isPresent()) {
						Student student = optionalStudent.get();
						student.setWard(ward);
						studentRepository.save(student);
						responseDTO.setResponseCode(Status.SUCCESS);
						responseDTO.setMessage("Students added to the ward successfully");
					} else {
						responseDTO.setResponseCode(Status.FAILURE);
						responseDTO.setMessage("students not found");
					}
				}
			} catch (Exception exception) {
				throw new StudCareDataException("Failed to add students to the ward");
			}
		} else {
			responseDTO.setResponseCode(Status.FAILURE);
			responseDTO.setMessage("Ward not found");
		}
		return responseDTO;
	}

	private StudentDTO adaptStudent(Student student) {
		StudentDTO dto = new StudentDTO();
		dto.setStudentId(student.getStudentId());
		if (!ObjectUtils.isEmpty(student.getSchoolClass())){
			dto.setSchoolClassDTO(schoolClassAdapter.adapt(student.getSchoolClass()));
		}
		dto.setUserDTO(userAdapter.adapt(student.getUser()));
		return dto;
	}
}