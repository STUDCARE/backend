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
import com.studcare.model.AddTeacherToSubjectRequestDTO;
import com.studcare.model.CreateSubjectRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
 class SubjectServiceTest {

	@Autowired
	private SubjectService subjectService;

	@MockBean
	private SubjectRepository subjectRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private SubjectTeacherRepository subjectTeacherRepository;

	@MockBean
	private CreateSubjectRequestAdapter createSubjectRequestAdapter;

	@MockBean
	private AddTeacherToSubjectRequestAdapter addTeacherToSubjectRequestAdapter;

	@MockBean
	private ResponseAdapter responseAdapter;

	@Test
	 void testCreateSubject_Success() {
		HttpRequestData requestData = new HttpRequestData(); // Assume properly initialized
		CreateSubjectRequestDTO requestDTO = new CreateSubjectRequestDTO();
		requestDTO.setSubjectName("Mathematics");
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);

		when(createSubjectRequestAdapter.adapt(requestData)).thenReturn(requestDTO);
		when(subjectRepository.existsBySubjectName("Mathematics")).thenReturn(false);
		when(subjectRepository.save(new Subject())).thenReturn(new Subject());
		when(responseAdapter.adapt(any())).thenReturn(httpResponseData);

		ResponseEntity<Object> responseEntity = subjectService.createSubject(requestData);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	 void testCreateSubject_ValidationError_SubjectExists() {
		HttpRequestData requestData = new HttpRequestData(); // Assume properly initialized
		CreateSubjectRequestDTO requestDTO = new CreateSubjectRequestDTO();
		requestDTO.setSubjectName("Mathematics");

		when(createSubjectRequestAdapter.adapt(requestData)).thenReturn(requestDTO);
		when(subjectRepository.existsBySubjectName("Mathematics")).thenReturn(true);

		ResponseEntity<Object> responseEntity = subjectService.createSubject(requestData);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	 void testAddTeacherToSubject_Success() {
		HttpRequestData requestData = new HttpRequestData(); // Assume properly initialized
		AddTeacherToSubjectRequestDTO requestDTO = new AddTeacherToSubjectRequestDTO();
		requestDTO.setSubject("Mathematics");
		requestDTO.setTeacher("teacher@mail.com");

		Subject subject = new Subject();
		subject.setSubjectName("Mathematics");

		User teacher = new User();
		teacher.setEmail("teacher@mail.com");
		teacher.setRole(UserRole.TEACHER);

		SubjectTeacher subjectTeacher = new SubjectTeacher();
		SubjectTeacher.SubjectTeacherKey key = new SubjectTeacher.SubjectTeacherKey();
		key.setSubjectID(subject.getSubjectID());
		key.setTeacherID(teacher.getUserID());
		subjectTeacher.setId(key);
		subjectTeacher.setSubject(subject);
		subjectTeacher.setTeacher(teacher);

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);

		when(addTeacherToSubjectRequestAdapter.adapt(requestData)).thenReturn(requestDTO);
		when(subjectRepository.findBySubjectName("Mathematics")).thenReturn(Optional.of(subject));
		when(userRepository.findByEmail("teacher@mail.com")).thenReturn(Optional.of(teacher));
		when(subjectTeacherRepository.existsBySubjectAndTeacher(subject, teacher)).thenReturn(false);
		when(responseAdapter.adapt(any())).thenReturn(httpResponseData);

		ResponseEntity<Object> responseEntity = subjectService.addTeacher(requestData);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	 void testAddTeacherToSubject_ValidationError_SubjectNotFound() {
		HttpRequestData requestData = new HttpRequestData();
		AddTeacherToSubjectRequestDTO requestDTO = new AddTeacherToSubjectRequestDTO();
		requestDTO.setSubject("Mathematics");

		when(addTeacherToSubjectRequestAdapter.adapt(requestData)).thenReturn(requestDTO);
		when(subjectRepository.findBySubjectName("Mathematics")).thenReturn(Optional.empty());

		ResponseEntity<Object> responseEntity = subjectService.addTeacher(requestData);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	 void testAddTeacherToSubject_ValidationError_UserNotTeacher() {
		HttpRequestData requestData = new HttpRequestData();
		AddTeacherToSubjectRequestDTO requestDTO = new AddTeacherToSubjectRequestDTO();
		requestDTO.setSubject("Mathematics");
		requestDTO.setTeacher("user@mail.com");

		Subject subject = new Subject();
		subject.setSubjectName("Mathematics");

		User user = new User();
		user.setEmail("user@mail.com");
		user.setRole(UserRole.STUDENT);

		when(addTeacherToSubjectRequestAdapter.adapt(requestData)).thenReturn(requestDTO);
		when(subjectRepository.findBySubjectName("Mathematics")).thenReturn(Optional.of(subject));
		when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));

		ResponseEntity<Object> responseEntity = subjectService.addTeacher(requestData);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}

	@Test
	 void testGetAllSubjects_Success() {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);

		when(subjectRepository.findAll()).thenReturn(List.of(new Subject()));
		when(responseAdapter.adapt(any())).thenReturn(httpResponseData);

		ResponseEntity<Object> responseEntity = subjectService.getAllSubjects();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

}
