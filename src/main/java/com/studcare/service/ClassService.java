package com.studcare.service;

import com.studcare.adapter.AddStudentRequestAdapter;
import com.studcare.adapter.AddSubjectsToClassRequestAdapter;
import com.studcare.adapter.ClassRequestAdapter;
import com.studcare.adapter.ResponseAdapter;
import com.studcare.adapter.YearTermResultRequestAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.SchoolClassAdapter;
import com.studcare.data.jpa.adaptor.SubjectAdapter;
import com.studcare.data.jpa.adaptor.UserAdapter;
import com.studcare.data.jpa.adaptor.WardAdapter;
import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.data.jpa.dto.StudentDTO;
import com.studcare.data.jpa.dto.SubjectTeacherDTO;
import com.studcare.data.jpa.entity.ClassSubjectAssignment;
import com.studcare.data.jpa.entity.SchoolClass;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.StudentSubjectEnrollment;
import com.studcare.data.jpa.entity.Subject;
import com.studcare.data.jpa.entity.SubjectResult;
import com.studcare.data.jpa.entity.TermResult;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.repository.ClassSubjectAssignmentRepository;
import com.studcare.data.jpa.repository.SchoolClassRepository;
import com.studcare.data.jpa.repository.StudentRepository;
import com.studcare.data.jpa.repository.StudentSubjectEnrollmentRepository;
import com.studcare.data.jpa.repository.SubjectRepository;
import com.studcare.data.jpa.repository.SubjectResultRepository;
import com.studcare.data.jpa.repository.SubjectTeacherRepository;
import com.studcare.data.jpa.repository.TermResultRepository;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.exception.StudCareDataException;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.AddStudentsRequestDTO;
import com.studcare.model.AddSubjectsToClassRequestDTO;
import com.studcare.model.ClassDetailsDTO;
import com.studcare.model.ClassRequestDTO;
import com.studcare.model.ClassResultsDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import com.studcare.model.StudentResultsDTO;
import com.studcare.model.SubjectResultDTO;
import com.studcare.model.TermResultDTO;
import com.studcare.model.UserDTO;
import com.studcare.model.YearTermDTO;
import com.studcare.validator.ClassValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j @Service public class ClassService {

	@Autowired private SchoolClassRepository schoolClassRepository;
	@Autowired private StudentRepository studentRepository;
	@Autowired private ClassRequestAdapter classRequestAdapter;
	@Autowired private AddStudentRequestAdapter addStudentRequestAdapter;
	@Autowired private ResponseAdapter classResponseAdapter;
	@Autowired @Lazy private SchoolClassAdapter schoolClassAdapter;
	@Autowired private AddSubjectsToClassRequestAdapter addSubjectsToClassRequestAdapter;
	@Autowired private ClassValidator classValidator;
	@Autowired private SubjectAdapter subjectAdapter;
	@Autowired private UserRepository userRepository;
	@Autowired private SubjectRepository subjectRepository;
	@Autowired private ClassSubjectAssignmentRepository classSubjectAssignmentRepository;
	@Autowired private StudentSubjectEnrollmentRepository studentSubjectEnrollmentRepository;
	@Autowired private UserAdapter userAdapter;
	@Autowired private YearTermResultRequestAdapter yearTermResultRequestAdapter;
	@Autowired private ResponseAdapter responseAdapter;
	@Autowired private WardAdapter wardAdapter;
	@Autowired private TermResultRepository termResultRepository;
	@Autowired private SubjectResultRepository subjectResultRepository;
	@Autowired private SubjectTeacherRepository subjectTeacherRepository;

	@Transactional
	public ResponseEntity<Object> createClass(HttpRequestData httpRequestData) {
		log.info("AccountService.class() initiated");
		ResponseEntity<Object> responseEntity;
		ResponseDTO classResponseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			ClassRequestDTO classRequestDTO = classRequestAdapter.adapt(httpRequestData);
			classValidator.validate(classRequestDTO);
			classResponseDTO = addClass(classRequestDTO);
			httpResponseData = classResponseAdapter.adapt(classResponseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("AccountService.class()  finished for {}", classRequestDTO.getSchoolClassDTO().getClassName());
		} catch (StudCareValidationException exception) {
			httpResponseData.setResponseBody(exception.getMessage());
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.class()  an validation error occurred while processing the request", exception);
		} catch (StudCareRuntimeException exception) {
			httpResponseData.setResponseBody(exception.getMessage());
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.class()  an runtime error occurred while processing the request", exception);
		} catch (Exception exception) {
			httpResponseData.setResponseBody(exception.getMessage());
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			responseEntity = createResponseEntity(httpResponseData);
			log.error("AccountService.class()  an error occurred while processing the request", exception);
		}
		return responseEntity;
	}

	@Transactional
	private ResponseDTO addClass(ClassRequestDTO classRequestDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		User classTeacher = userRepository.findByEmail(classRequestDTO.getSchoolClassDTO().getClassTeacher().getEmail())
				.orElseThrow(() -> new StudCareValidationException("Class teacher not found"));
		if (Boolean.TRUE.equals(classTeacher.getIsClassTeacher())){
			log.error("User is already a class teacher", classTeacher.getEmail());
			responseDTO.setResponseCode(Status.FAILURE);
			responseDTO.setMessage("User is already a class teacher");
		}
		SchoolClassDTO schoolClassDTO = classRequestDTO.getSchoolClassDTO();
		UserDTO classTeacherDTO = userAdapter.adapt(classTeacher);
		schoolClassDTO.setClassTeacher(classTeacherDTO);
		SchoolClass schoolClass = schoolClassAdapter.adapt(schoolClassDTO);
		if (schoolClassRepository.findByClassName(schoolClass.getClassName()).isPresent()) {
			responseDTO.setResponseCode(Status.FAILURE);
			responseDTO.setMessage("Class already exists with name: " + schoolClass.getClassName());
		} else {
			try {
				schoolClassRepository.save(schoolClass);
				schoolClass.getClassTeacher().setIsClassTeacher(true);
				userRepository.save(schoolClass.getClassTeacher());
				responseDTO.setResponseCode(Status.SUCCESS);
				responseDTO.setMessage("Class created successfully for " + schoolClass.getClassName());
			} catch (Exception exception) {
				log.error("ClassService.addClass error occurred ", exception);
				throw new StudCareDataException("Failed saving class to the database");
			}
		}
		return responseDTO;
	}

	@Transactional
	public ResponseEntity<Object> addStudents(HttpRequestData httpRequestData) {
		log.info("ClassService.addStudents() initiated");
		ResponseEntity<Object> responseEntity;
		ResponseDTO responseDTO;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			AddStudentsRequestDTO addStudentsRequestDTO = addStudentRequestAdapter.adapt(httpRequestData);
			responseDTO = addStudentsToClass(addStudentsRequestDTO.getClassName(), addStudentsRequestDTO.getStudents().getStudentEmails());
			httpResponseData = classResponseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("ClassService.addStudents() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.addStudents() a validation error occurred while processing the request", exception);
		} catch (StudCareRuntimeException exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.addStudents() a runtime error occurred while processing the request", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.addStudents() an error occurred while processing the request", exception);
		}
		return responseEntity;
	}

	@Transactional
	private ResponseDTO addStudentsToClass(String className, List<String> studentEmails) {
		ResponseDTO responseDTO = new ResponseDTO();
		Optional<SchoolClass> optionalSchoolClass = schoolClassRepository.findByClassName(className);
		if (optionalSchoolClass.isPresent()) {
			SchoolClass schoolClass = optionalSchoolClass.get();
			try {
				for (String studentEmail : studentEmails) {
					Optional<Student> optionalStudent = studentRepository.findByUser_Email(studentEmail);
					if (optionalStudent.isPresent()) {
						Student student = optionalStudent.get();
						student.setSchoolClass(schoolClass);
						studentRepository.save(student);
						if (!ObjectUtils.isEmpty(schoolClass.getSubjects())) {
							List<ClassSubjectAssignment> classSubjects = classSubjectAssignmentRepository.findBySchoolClass(schoolClass);
							for (ClassSubjectAssignment classSubject : classSubjects) {
								StudentSubjectEnrollment enrollment = new StudentSubjectEnrollment();
								enrollment.setStudent(student);
								enrollment.setSubject(classSubject.getSubject());
								enrollment.setSchoolClass(schoolClass);
								studentSubjectEnrollmentRepository.save(enrollment);
							}
						}
						responseDTO.setResponseCode(Status.SUCCESS);
						responseDTO.setMessage("Students added to the class and mapped to subjects successfully");
					}else {
						log.error("Student not found" + studentEmail);
						responseDTO.setResponseCode(Status.FAILURE);
						responseDTO.setMessage("Student not found");
					}
				}
			} catch (Exception exception) {
				throw new StudCareDataException("Failed to add students to the class and map to subjects");
			}
		} else {
			responseDTO.setResponseCode(Status.FAILURE);
		}
		return responseDTO;
	}

	@Transactional
	public ResponseEntity<Object> addSubjectsToClass(HttpRequestData httpRequestData) {
		log.info("ClassService.addSubjectsToClass() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			AddSubjectsToClassRequestDTO addSubjectsToClassRequestDTO = addSubjectsToClassRequestAdapter.adapt(httpRequestData);
			ResponseDTO responseDTO = addSubjectsAndTeachersToClass(addSubjectsToClassRequestDTO);
			httpResponseData = classResponseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("ClassService.addSubjectsToClass() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.addSubjectsToClass() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.addSubjectsToClass() an error occurred", exception);
		}
		return responseEntity;
	}

	@Transactional
	private ResponseDTO addSubjectsAndTeachersToClass(AddSubjectsToClassRequestDTO subjectTeachersRequest) {
		SchoolClass schoolClass = schoolClassRepository.findByClassName(subjectTeachersRequest.getClassName()).orElseThrow(() -> new StudCareValidationException("Class not found"));
		for (String subjectName : subjectTeachersRequest.getSubjects().getSubjects()) {
			Subject subject = subjectRepository.findBySubjectName(subjectName).orElseThrow(() -> new StudCareDataException("Subject not found"));
			if (classSubjectAssignmentRepository.existsBySchoolClassAndSubject(schoolClass, subject)) {
				throw new StudCareValidationException("Subject already assigned to this class");
			}
			ClassSubjectAssignment assignment = new ClassSubjectAssignment();
			ClassSubjectAssignment.ClassSubjectAssignmentKey key = new ClassSubjectAssignment.ClassSubjectAssignmentKey();
			key.setClassID(schoolClass.getClassID());
			key.setSubjectID(subject.getSubjectID());
			assignment.setId(key);
			assignment.setSchoolClass(schoolClass);
			assignment.setSubject(subject);
			classSubjectAssignmentRepository.save(assignment);
		}
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		responseDTO.setMessage("Subjects and teachers added to the class successfully");
		return responseDTO;
	}

	@Transactional
	public ResponseEntity<Object> getStudentsInClass(String className) {
		log.info("ClassService.getStudentsInClass() initiated");
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			SchoolClass schoolClass = schoolClassRepository.findByClassName(className).orElseThrow(() -> new StudCareDataException("Class not found"));
			List<Student> students = studentRepository.findBySchoolClass(schoolClass);
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Students in class retrieved successfully");
			responseDTO.setData(students);
			httpResponseData = classResponseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("ClassService.getStudentsInClass() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.getStudentsInClass() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.getStudentsInClass() an error occurred", exception);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getClassesForTeacherAndSubject(String teacherId, String subjectId) {
		log.info("ClassService.getClassesForTeacherAndSubject() initiated for teacher ID: {} and subject ID: {}", teacherId, subjectId);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			User teacher = userRepository.findByEmail(teacherId).orElseThrow(() -> new StudCareDataException("Teacher not found"));
			Subject subject = subjectRepository.findBySubjectName(subjectId).orElseThrow(() -> new StudCareDataException("Subject not found"));
			if (!subjectTeacherRepository.existsBySubjectAndTeacher(subject, teacher)) {
				throw new StudCareValidationException("This Teacher does not teach this subject");
			}
			List<ClassSubjectAssignment> assignments = classSubjectAssignmentRepository.findByTeacherAndSubject(teacher, subject).orElseThrow(() -> new StudCareDataException("data service error"));
			if (assignments.isEmpty()) {
				throw new StudCareValidationException("Teacher Does not teach this subject to any class");
			}
			List<SchoolClassDTO> classDTOs = assignments.stream().map(assignment -> schoolClassAdapter.adapt(assignment.getSchoolClass())).collect(Collectors.toList());
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Teachers for subject retrieved successfully");
			responseDTO.setData(classDTOs);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("ClassService.getClassesForTeacherAndSubject() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.getClassesForTeacherAndSubject() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	@Transactional
	public ResponseEntity<Object> getClassDetails(String className) {
		log.info("ClassService.getClassDetails() initiated for class name: {}", className);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			SchoolClass schoolClass = schoolClassRepository.findByClassName(className).orElseThrow(() -> new StudCareDataException("Class not found"));
			List<Student> students = studentRepository.findBySchoolClass(schoolClass);
			List<ClassSubjectAssignment> assignments = classSubjectAssignmentRepository.findBySchoolClass(schoolClass);
			Map<Subject, User> subjectTeacherMap = assignments.stream().collect(Collectors.toMap(ClassSubjectAssignment::getSubject, ClassSubjectAssignment::getTeacher));
			ClassDetailsDTO classDetails = new ClassDetailsDTO();
			classDetails.setClassId(schoolClass.getClassID());
			classDetails.setClassName(schoolClass.getClassName());
			classDetails.setClassTeacher(userAdapter.adapt(schoolClass.getClassTeacher()));
			classDetails.setStudents(students.stream().map(this::adaptStudent).collect(Collectors.toList()));
			classDetails.setSubjectTeachers(subjectTeacherMap.entrySet().stream()
					.map(entry -> new SubjectTeacherDTO(subjectAdapter.adapt(entry.getKey()), userAdapter.adapt(entry.getValue())))
					.collect(Collectors.toList()));
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Class details retrieved successfully");
			responseDTO.setData(Collections.singletonList(classDetails));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("ClassService.getClassDetails() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.getClassDetails() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getClassResults(String className,  String requestBody) {
		YearTermDTO yearTermDTO = yearTermResultRequestAdapter.adapt(requestBody);
		log.info("ClassService.getClassResults() initiated for class ID: {}, academic year: {}, term: {}", className, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm());
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			SchoolClass schoolClass = schoolClassRepository.findByClassName(className).orElseThrow(() -> new StudCareDataException("Class not found"));
			List<Student> students = studentRepository.findBySchoolClass(schoolClass);
			ClassResultsDTO classResultsDTO = new ClassResultsDTO();
			classResultsDTO.setClassId(schoolClass.getClassID());
			classResultsDTO.setClassName(schoolClass.getClassName());
			List<StudentResultsDTO> studentResultsDTOs = new ArrayList<>();
			for (Student student : students) {
				StudentResultsDTO studentResultsDTO = new StudentResultsDTO();
				studentResultsDTO.setStudentId(student.getStudentId());
				studentResultsDTO.setStudentName(student.getUser().getUsername());
				TermResult termResult = termResultRepository.findByStudentAndAcademicYearAndTermNumber(student, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm()).orElse(null);
				List<SubjectResultDTO> subjectResultDTOs = new ArrayList<>();
				if (termResult != null) {
					List<SubjectResult> subjectResults = subjectResultRepository.findByTermResult(termResult);
					for (SubjectResult subjectResult : subjectResults) {
						SubjectResultDTO subjectResultDTO = new SubjectResultDTO();
						subjectResultDTO.setSubjectId(subjectResult.getSubject().getSubjectID());
						subjectResultDTO.setSubjectName(subjectResult.getSubject().getSubjectName());
						subjectResultDTO.setMarks(subjectResult.getMarks());
						subjectResultDTO.setGrade(subjectResult.getGrade());
						subjectResultDTO.setTeacherNote(subjectResult.getTeacherNote());
						subjectResultDTOs.add(subjectResultDTO);
					}
				}
				studentResultsDTO.setSubjectResults(subjectResultDTOs);
				studentResultsDTOs.add(studentResultsDTO);
			}
			classResultsDTO.setStudentResults(studentResultsDTOs);
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Class results retrieved successfully");
			responseDTO.setData(Collections.singletonList(classResultsDTO));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("ClassService.getClassResults() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.getClassResults() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> addTeacherToClassSubject(String teacherEmail, String subjectName, String className) {
		log.info("ClassService.addTeacherToClassSubject() initiated for teacher ID: {} and subject ID: {} and class ID: {}", teacherEmail, subjectName, className);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			ResponseDTO responseDTO = saveTeacherToClassSubject(teacherEmail, subjectName, className);
			httpResponseData = classResponseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
			log.info("ClassService.addTeacherToClassSubject() finished successfully");
		} catch (StudCareValidationException exception) {
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.addTeacherToClassSubject() a validation error occurred", exception);
		} catch (Exception exception) {
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
			log.error("ClassService.addTeacherToClassSubject() an error occurred", exception);
		}
		return responseEntity;
	}

	private ResponseDTO saveTeacherToClassSubject(String teacherEmail, String subjectName, String className) {
		SchoolClass schoolClass = schoolClassRepository.findByClassName(className).orElseThrow(() -> new StudCareValidationException("Class not found"));
		Subject subject = subjectRepository.findBySubjectName(subjectName).orElseThrow(() -> new StudCareDataException("Subject not found"));
		User teacher = userRepository.findByEmail(teacherEmail).orElseThrow(() -> new StudCareDataException("Teacher not found"));
		if (!subjectTeacherRepository.existsBySubjectAndTeacher(subject, teacher)) {
			throw new StudCareValidationException("This Teacher does not teach this subject");
		}
		ClassSubjectAssignment assignment = classSubjectAssignmentRepository.findBySchoolClassAndSubject(schoolClass, subject).orElseThrow(() -> new StudCareDataException("Subject is not bean thought in this class"));
		ClassSubjectAssignment.ClassSubjectAssignmentKey key = assignment.getId();
		assignment.setId(key);
		assignment.setSchoolClass(schoolClass);
		assignment.setSubject(subject);
		assignment.setTeacher(teacher);
		classSubjectAssignmentRepository.save(assignment);
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(Status.SUCCESS);
		responseDTO.setMessage("Teacher added to the class subject successfully");
		return responseDTO;
	}

	private StudentDTO adaptStudent(Student student) {
		StudentDTO dto = new StudentDTO();
		dto.setStudentId(student.getStudentId());
		if (!ObjectUtils.isEmpty(student.getWard())){
			dto.setWardDTO(wardAdapter.adapt(student.getWard()));
		}
		dto.setUserDTO(userAdapter.adapt(student.getUser()));
		return dto;
	}

	@Transactional
	public ResponseEntity<Object> calculateClassResults(String className, YearTermDTO yearTermDTO) {
		log.info("ClassService.calculateClassResults() initiated for class name: {}, academic year: {}, term: {}", className, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm());
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			SchoolClass schoolClass = schoolClassRepository.findByClassName(className).orElseThrow(() -> new StudCareDataException("Class not found"));
			List<Student> students = studentRepository.findBySchoolClass(schoolClass);
			List<TermResultDTO> studentResults = new ArrayList<>();
			for (Student student : students) {
				TermResultDTO result = calculateStudentResult(student, yearTermDTO);
				studentResults.add(result);
			}
			// Sort students by total marks in descending order
			studentResults.sort((a, b) -> b.getTotalMarks().compareTo(a.getTotalMarks()));
			// Assign class ranks
			for (int i = 0; i < studentResults.size(); i++) {
				TermResultDTO result = studentResults.get(i);
				int rank = i + 1;
				result.setClassRank(rank);
				updateTermResultWithRank(result.getStudentId(), yearTermDTO, rank);
			}
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Class results calculated and ranks updated successfully");
			responseDTO.setData(Collections.singletonList(studentResults));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("ClassService.calculateClassResults() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.calculateClassResults() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	private TermResultDTO calculateStudentResult(Student student, YearTermDTO yearTermDTO) {
		TermResult termResult = termResultRepository.findByStudentAndAcademicYearAndTermNumber(student, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm())
				.orElseThrow(() -> new StudCareDataException("Term result not found"));
		List<SubjectResult> subjectResults = subjectResultRepository.findByTermResult(termResult);
		int totalMarks = 0;
		for (SubjectResult subjectResult : subjectResults) {
			totalMarks += Integer.parseInt(subjectResult.getMarks());
		}
		TermResultDTO result = new TermResultDTO();
		result.setStudentId(student.getStudentId());
		result.setStudentName(student.getUser().getEmail());
		result.setTotalMarks(totalMarks);
		return result;
	}

	private void updateTermResultWithRank(Long studentId, YearTermDTO yearTermDTO, int rank) {
		Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudCareDataException("student not found "));
		TermResult termResult = termResultRepository.findByStudentAndAcademicYearAndTermNumber(student, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm())
				.orElseThrow(() -> new StudCareDataException("Term result not found for student"));
		termResult.setClassRank(String.valueOf(rank));
		termResultRepository.save(termResult);
	}
}
