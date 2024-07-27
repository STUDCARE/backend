package com.studcare.service;

import com.studcare.adapter.AddTeacherToSubjectRequestAdapter;
import com.studcare.adapter.CreateSubjectRequestAdapter;
import com.studcare.adapter.ResponseAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.entity.Subject;
import com.studcare.data.jpa.entity.SubjectTeacher;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.entity.UserRole;
import com.studcare.data.jpa.repository.SubjectRepository;
import com.studcare.data.jpa.repository.SubjectTeacherRepository;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.exception.StudCareDataException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.AddTeacherToSubjectRequestDTO;
import com.studcare.model.CreateSubjectRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j
@Service
public class SubjectService {
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SubjectTeacherRepository subjectTeacherRepository;
	@Autowired
	private CreateSubjectRequestAdapter createSubjectRequestAdapter;
	@Autowired
	private AddTeacherToSubjectRequestAdapter addTeacherToSubjectRequestAdapter;
	@Autowired
	private ResponseAdapter responseAdapter;

	public ResponseEntity<Object> createSubject(HttpRequestData httpRequestData) {
		log.info("SubjectService.createSubject() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			CreateSubjectRequestDTO createSubjectRequestDTO = createSubjectRequestAdapter.adapt(httpRequestData);
			ResponseDTO responseDTO = addNewSubject(createSubjectRequestDTO);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("SubjectService.createSubject() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.createSubject() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.createSubject() an error occurred", exception);
		}
		return responseEntity;
	}

	private ResponseDTO addNewSubject(CreateSubjectRequestDTO createSubjectRequestDTO) {
		if (subjectRepository.existsBySubjectName(createSubjectRequestDTO.getSubjectName())) {
			throw new StudCareValidationException("Subject already exists");
		}
		Subject subject = new Subject();
		subject.setSubjectName(createSubjectRequestDTO.getSubjectName());
		try {
			subjectRepository.save(subject);
		} catch (Exception e) {
			throw new StudCareDataException("Failed to save new subject");
		}
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		responseDTO.setMessage("Subject created successfully");
		return responseDTO;
	}

	public ResponseEntity<Object> addTeacher(HttpRequestData httpRequestData) {
		log.info("SubjectService.addTeacher() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			AddTeacherToSubjectRequestDTO addTeacherToSubjectRequestDTO = addTeacherToSubjectRequestAdapter.adapt(httpRequestData);
			ResponseDTO responseDTO = addTeacherToSubject(addTeacherToSubjectRequestDTO);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("SubjectService.addTeacher() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.addTeacher() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.addTeacher() an error occurred", exception);
		}
		return responseEntity;
	}

	private ResponseDTO addTeacherToSubject(AddTeacherToSubjectRequestDTO addTeacherToSubjectRequestDTO) {
		Subject subject = subjectRepository.findBySubjectName(addTeacherToSubjectRequestDTO.getSubject()).orElseThrow(() -> new StudCareValidationException("Subject not found"));
		User teacher = userRepository.findByEmail(addTeacherToSubjectRequestDTO.getTeacher()).orElseThrow(() -> new StudCareValidationException("Teacher not found"));
		if (!teacher.getRole().equals(UserRole.TEACHER)) {
			throw new StudCareValidationException("User is not a teacher");
		}
		if (subjectTeacherRepository.existsBySubjectAndTeacher(subject, teacher)) {
			throw new StudCareValidationException("Teacher is already assigned to this subject");
		}
		SubjectTeacher subjectTeacher = new SubjectTeacher();
		SubjectTeacher.SubjectTeacherKey key = new SubjectTeacher.SubjectTeacherKey();
		key.setSubjectID(subject.getSubjectID());
		key.setTeacherID(teacher.getUserID());
		subjectTeacher.setId(key);
		subjectTeacher.setSubject(subject);
		subjectTeacher.setTeacher(teacher);
		try {
			subjectTeacherRepository.save(subjectTeacher);
		} catch (Exception e) {
			throw new StudCareDataException("Failed to assign teacher to subject");
		}
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		responseDTO.setMessage("Teacher assigned to subject successfully");
		return responseDTO;
	}

	public ResponseEntity<Object> getAllSubjects() {
		log.info("SubjectService.getAllSubjects() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			List<Subject> subjects = subjectRepository.findAll();
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Subjects retrieved successfully");
			responseDTO.setData(subjects);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("SubjectService.getAllSubjects() finished successfully");
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.getAllSubjects() an error occurred", exception);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getSubjectsForTeacher(String teacherEmail) {
		log.info("SubjectService.getSubjectsForTeacher() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			User teacher = userRepository.findByEmail(teacherEmail).orElseThrow(() -> new StudCareValidationException("Teacher not found"));
			List<Subject> subjects = subjectTeacherRepository.findByTeacher(teacher)
					.stream()
					.map(SubjectTeacher::getSubject)
					.collect(Collectors.toList());
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Subjects for teacher retrieved successfully");
			responseDTO.setData(subjects);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("SubjectService.getSubjectsForTeacher() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.getSubjectsForTeacher() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.getSubjectsForTeacher() an error occurred", exception);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getTeachersForSubject(String subjectName) {
		log.info("SubjectService.getTeachersForSubject() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Subject subject = subjectRepository.findBySubjectName(subjectName).orElseThrow(() -> new StudCareValidationException("Subject not found"));
			List<User> teachers = subjectTeacherRepository.findBySubject(subject)
					.stream()
					.map(SubjectTeacher::getTeacher)
					.collect(Collectors.toList());
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Teachers for subject retrieved successfully");
			responseDTO.setData(teachers);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("SubjectService.getTeachersForSubject() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.getTeachersForSubject() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("SubjectService.getTeachersForSubject() an error occurred", exception);
		}
		return responseEntity;
	}
}