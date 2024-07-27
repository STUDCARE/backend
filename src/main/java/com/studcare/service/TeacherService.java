package com.studcare.service;

import com.studcare.adapter.ResponseAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.SchoolClassAdapter;
import com.studcare.data.jpa.adaptor.SubjectAdapter;
import com.studcare.data.jpa.entity.ClassSubjectAssignment;
import com.studcare.data.jpa.entity.SchoolClass;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.Subject;
import com.studcare.data.jpa.entity.SubjectResult;
import com.studcare.data.jpa.entity.TermResult;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.repository.ClassSubjectAssignmentRepository;
import com.studcare.data.jpa.repository.SchoolClassRepository;
import com.studcare.data.jpa.repository.StudentRepository;
import com.studcare.data.jpa.repository.SubjectRepository;
import com.studcare.data.jpa.repository.SubjectResultRepository;
import com.studcare.data.jpa.repository.TermResultRepository;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.HttpResponseData;
import com.studcare.model.ResponseDTO;
import com.studcare.model.StudentResultDTO;
import com.studcare.model.SubjectClassesDTO;
import com.studcare.model.SubjectResultsRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j
@Service
public class TeacherService {

	@Autowired private SchoolClassRepository schoolClassRepository;
	@Autowired private StudentRepository studentRepository;
	@Autowired @Lazy private SchoolClassAdapter schoolClassAdapter;
	@Autowired private ClassService classService;
	@Autowired private SubjectAdapter subjectAdapter;
	@Autowired private UserRepository userRepository;
	@Autowired private SubjectRepository subjectRepository;
	@Autowired private ClassSubjectAssignmentRepository classSubjectAssignmentRepository;
	@Autowired private ResponseAdapter responseAdapter;
	@Autowired private SubjectResultRepository subjectResultRepository;
	@Autowired private TermResultRepository termResultRepository;

	public ResponseEntity<Object> getTeacherSubjectsAndClasses(String teacherEmail) {
		log.info("ClassService.getTeacherSubjectsAndClasses() initiated for teacher ID: {}", teacherEmail);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			User teacher = userRepository.findByEmail(teacherEmail).orElseThrow(() -> new StudCareValidationException("Teacher not found"));
			List<ClassSubjectAssignment> assignments = classSubjectAssignmentRepository.findByTeacher(teacher);
			Map<Subject, List<SchoolClass>> subjectClassesMap = assignments.stream().collect(
					Collectors.groupingBy(ClassSubjectAssignment::getSubject, Collectors.mapping(ClassSubjectAssignment::getSchoolClass, Collectors.toList())));
			List<SubjectClassesDTO> subjectClassesList = subjectClassesMap.entrySet().stream()
					.map(entry -> new SubjectClassesDTO(subjectAdapter.adapt(entry.getKey()), entry.getValue().stream().map(schoolClassAdapter::adapt).collect(Collectors.toList()))).collect(Collectors.toList());
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Teacher subjects and classes retrieved successfully");
			responseDTO.setData(subjectClassesList);
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("ClassService.getTeacherSubjectsAndClasses() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.getTeacherSubjectsAndClasses() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getClassTeacherDetails(String teacher) {
		log.info("ClassService.getClassTeacherDetails() initiated for user ID: {}", teacher);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			User user = userRepository.findByEmail(teacher).orElseThrow(() -> new StudCareValidationException("User not found"));
			if (Boolean.TRUE.equals(user.getIsClassTeacher())) {
				SchoolClass schoolClass = schoolClassRepository.findByClassTeacher(user).orElseThrow(() -> new StudCareValidationException("Class not found for this class teacher"));
				return classService.getClassDetails(schoolClass.getClassName());
			} else {
				ResponseDTO responseDTO = new ResponseDTO();
				responseDTO.setResponseCode(Status.FAILURE);
				responseDTO.setMessage("User is not a class teacher");
				httpResponseData = responseAdapter.adapt(responseDTO);
				responseEntity = createResponseEntity(httpResponseData);
			}
		} catch (StudCareValidationException exception) {
			log.error("ClassService.getClassTeacherDetails() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.getClassTeacherDetails() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> addSubjectResults(SubjectResultsRequestDTO resultsRequestDTO, String teacherEmail) {
		log.info("ClassService.addSubjectResults() initiated for subject ID: {}", resultsRequestDTO.getSubjectName());
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			User teacher = userRepository.findByEmail(teacherEmail).orElseThrow(() -> new StudCareValidationException("Teacher not found"));
			Subject subject = subjectRepository.findBySubjectName(resultsRequestDTO.getSubjectName()).orElseThrow(() -> new StudCareValidationException("Subject not found"));
			if (!classSubjectAssignmentRepository.existsByTeacherAndSubject(teacher, subject)) {
				throw new StudCareValidationException("Teacher is not assigned to this subject");
			}
			for (StudentResultDTO studentResult : resultsRequestDTO.getStudentResults()) {
				Student student = studentRepository.findByUser_Email(studentResult.getStudentEmail()).orElseThrow(() -> new StudCareValidationException("Student not found"));
				TermResult termResult = termResultRepository.findByStudentAndAcademicYearAndTermNumber(student, resultsRequestDTO.getAcademicYear(),
						resultsRequestDTO.getTermNumber()).orElseGet(() -> {
					TermResult newTermResult = new TermResult();
					newTermResult.setStudent(student);
					newTermResult.setAcademicYear(resultsRequestDTO.getAcademicYear());
					newTermResult.setTermNumber(resultsRequestDTO.getTermNumber());
					return termResultRepository.save(newTermResult);
				});
				SubjectResult subjectResult = subjectResultRepository.findByTermResultAndSubject(termResult, subject).orElseGet(() -> {
					SubjectResult newSubjectResult = new SubjectResult();
					newSubjectResult.setTermResult(termResult);
					newSubjectResult.setSubject(subject);
					newSubjectResult.setTeacher(teacher);
					return newSubjectResult;
				});
				subjectResult.setMarks(studentResult.getMarks());
				subjectResult.setGrade(studentResult.getGrade());
				subjectResult.setTeacherNote(studentResult.getTeacherNote());
				subjectResultRepository.save(subjectResult);
			}
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Subject results added successfully");
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("ClassService.addSubjectResults() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.addSubjectResults() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}
}
