package com.studcare.service;

import com.studcare.adapter.*;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.*;
import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.data.jpa.dto.SubjectTeacherDTO;
import com.studcare.data.jpa.entity.*;
import com.studcare.data.jpa.repository.*;
import com.studcare.exception.*;
import com.studcare.model.*;
import com.studcare.validator.ClassValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClassServiceTest {

	@InjectMocks
	private ClassService classService;

	@Mock
	private SchoolClassRepository schoolClassRepository;
	@Mock
	private StudentRepository studentRepository;
	@Mock
	private ClassRequestAdapter classRequestAdapter;
	@Mock
	private AddStudentRequestAdapter addStudentRequestAdapter;
	@Mock
	private ResponseAdapter classResponseAdapter;
	@Mock
	private SchoolClassAdapter schoolClassAdapter;
	@Mock
	private AddSubjectsToClassRequestAdapter addSubjectsToClassRequestAdapter;
	@Mock
	private ClassValidator classValidator;
	@Mock
	private SubjectAdapter subjectAdapter;
	@Mock
	private UserRepository userRepository;
	@Mock
	private SubjectRepository subjectRepository;
	@Mock
	private ClassSubjectAssignmentRepository classSubjectAssignmentRepository;
	@Mock
	private StudentSubjectEnrollmentRepository studentSubjectEnrollmentRepository;
	@Mock
	private UserAdapter userAdapter;
	@Mock
	private YearTermResultRequestAdapter yearTermResultRequestAdapter;
	@Mock
	private ResponseAdapter responseAdapter;
	@Mock
	private WardAdapter wardAdapter;
	@Mock
	private TermResultRepository termResultRepository;
	@Mock
	private SubjectResultRepository subjectResultRepository;
	@Mock
	private SubjectTeacherRepository subjectTeacherRepository;

	private HttpRequestData httpRequestData;


	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);


	}

	@Test
	void testCreateClassSuccess() {
		ClassRequestDTO classRequestDTO = mock(ClassRequestDTO.class);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);
		UserDTO classTeacherDTO = new UserDTO();
		classTeacherDTO.setEmail("sample@gmail.com");
		SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
		schoolClassDTO.setClassName("class");
		schoolClassDTO.setClassTeacher(classTeacherDTO);
		User classTeacher = new User();
		classTeacher.setEmail("sample@gmail.com");
		SchoolClass schoolClass = new SchoolClass();
		schoolClass.setClassName("class");
		schoolClass.setClassTeacher(classTeacher);
		ResponseDTO responseDTO = new ResponseDTO();

		when(classRequestDTO.getSchoolClassDTO()).thenReturn(schoolClassDTO);
		when(classRequestAdapter.adapt(httpRequestData)).thenReturn(classRequestDTO);
		when(userRepository.findByEmail(any())).thenReturn(Optional.of(classTeacher));
		when(schoolClassAdapter.adapt(schoolClassDTO)).thenReturn(schoolClass);
		when(schoolClassRepository.findByClassName(any())).thenReturn(Optional.empty());
		when(schoolClassRepository.save(any())).thenReturn(schoolClass);
		when(userRepository.save(any())).thenReturn(classTeacher);
		when(classResponseAdapter.adapt(any())).thenReturn(httpResponseData		);

		ResponseEntity<Object> responseEntity = classService.createClass(httpRequestData);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		verify(classRequestAdapter).adapt(httpRequestData);
		verify(classValidator).validate(classRequestDTO);
		verify(schoolClassRepository).save(schoolClass);
		verify(userRepository).save(classTeacher);
	}

	@Test
	void testCreateClassValidationException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(classRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareValidationException("Validation error"));
		ResponseEntity<Object> response = classService.createClass(httpRequestData);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

}
