package com.studcare.service;

import com.studcare.adapter.MonthlyEvaluationResponseAdapter;
import com.studcare.adapter.ResponseAdapter;
import com.studcare.adapter.YearTermResultRequestAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.UserAdapter;
import com.studcare.data.jpa.entity.GradingScale;
import com.studcare.data.jpa.entity.MonthlyEvaluation;
import com.studcare.data.jpa.entity.SchoolClass;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.SubjectResult;
import com.studcare.data.jpa.entity.TermResult;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.repository.MonthlyEvaluationRepository;
import com.studcare.data.jpa.repository.StudentRepository;
import com.studcare.data.jpa.repository.SubjectResultRepository;
import com.studcare.data.jpa.repository.TermResultRepository;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.exception.StudCareDataException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.ClassTeacherNoteDTO;
import com.studcare.model.HttpResponseData;
import com.studcare.model.MonthlyEvaluationData;
import com.studcare.model.MonthlyEvaluationResponseDTO;
import com.studcare.model.MonthlyEvaluationsDTO;
import com.studcare.model.ResponseDTO;
import com.studcare.model.StudentResultsDTO;
import com.studcare.model.SubjectResultDTO;
import com.studcare.model.TermResultsDTO;
import com.studcare.model.UserDTO;
import com.studcare.model.YearResultsDTO;
import com.studcare.model.YearTermDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.studcare.util.CommonUtils.createResponseEntity;

@Slf4j
@Service
public class StudentService {
	@Autowired private StudentRepository studentRepository;
	@Autowired private ResponseAdapter responseAdapter;
	@Autowired private YearTermResultRequestAdapter yearTermResultRequestAdapter;
	@Autowired private TermResultRepository termResultRepository;
	@Autowired private SubjectResultRepository subjectResultRepository;
	@Autowired private UserAdapter userAdapter;
	@Autowired private UserRepository userRepository;
	@Autowired private MonthlyEvaluationResponseAdapter monthlyEvaluationResponseAdapter;
	@Autowired private MonthlyEvaluationRepository monthlyEvaluationRepository;

	public ResponseEntity<Object> getStudentResults(String studentEmail, String requestBody) {
		YearTermDTO yearTermDTO = yearTermResultRequestAdapter.adapt(requestBody);
		log.info("StudentService.getStudentResults() initiated for student ID: {}, academic year: {}, term: {}", studentEmail, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm());
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Student student = studentRepository.findByUser_Email(studentEmail).orElseThrow(() -> new StudCareDataException("Student not found"));
			StudentResultsDTO studentResultsDTO = new StudentResultsDTO();
			studentResultsDTO.setStudentId(student.getStudentId());
			studentResultsDTO.setStudentName(student.getUser().getUsername());
			YearResultsDTO yearResultsDTO = new YearResultsDTO();
			yearResultsDTO.setAcademicYear(yearTermDTO.getAcademicYear());
			TermResultsDTO termResultsDTO = new TermResultsDTO();
			termResultsDTO.setTermNumber(yearTermDTO.getTerm());
			List<SubjectResultDTO> subjectResultDTOs = new ArrayList<>();
			TermResult termResult = termResultRepository.findByStudentAndAcademicYearAndTermNumber(student, yearTermDTO.getAcademicYear(), yearTermDTO.getTerm())
					.orElseThrow(() -> new StudCareDataException("Term results not found"));
			if (termResult != null) {
				List<SubjectResult> subjectResults = subjectResultRepository.findByTermResult(termResult);
				for (SubjectResult subjectResult : subjectResults) {
					SubjectResultDTO subjectResultDTO = new SubjectResultDTO();
					subjectResultDTO.setSubjectId(subjectResult.getSubject().getSubjectID());
					subjectResultDTO.setSubjectName(subjectResult.getSubject().getSubjectName());
					subjectResultDTO.setMarks(subjectResult.getMarks());
					subjectResultDTO.setGrade(subjectResult.getGrade());
					subjectResultDTO.setTeacherNote(subjectResult.getTeacherNote());
					subjectResultDTO.setTeacherName(subjectResult.getTeacher().getEmail());
					subjectResultDTOs.add(subjectResultDTO);
				}
			}
			termResultsDTO.setSubjectResults(subjectResultDTOs);
			termResultsDTO.setClassRank(termResult.getClassRank());
			termResultsDTO.setTermNote(termResult.getClassTeacherNote());
			yearResultsDTO.setTermResults(Collections.singletonList(termResultsDTO));
			studentResultsDTO.setYearResults(Collections.singletonList(yearResultsDTO));
			studentResultsDTO.setSchoolClass(student.getSchoolClass());
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Student results retrieved successfully");
			responseDTO.setData(Collections.singletonList(studentResultsDTO));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("StudentService.getStudentResults() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("StudentService.getStudentResults() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getStudentYearResults(String studentEmail, String requestBody) {
		YearTermDTO yearTermDTO = yearTermResultRequestAdapter.adapt(requestBody);
		log.info("StudentService.getStudentYearResults() initiated for student ID: {}, academic year: {}", studentEmail, yearTermDTO.getAcademicYear());
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Student student = studentRepository.findByUser_Email(studentEmail).orElseThrow(() -> new StudCareDataException("Student not found"));
			StudentResultsDTO studentResultsDTO = new StudentResultsDTO();
			studentResultsDTO.setStudentId(student.getStudentId());
			studentResultsDTO.setStudentName(student.getUser().getUsername());
			YearResultsDTO yearResultsDTO = new YearResultsDTO();
			yearResultsDTO.setAcademicYear(yearTermDTO.getAcademicYear());
			List<TermResultsDTO> termResultsDTOs = new ArrayList<>();
			List<TermResult> termResults = termResultRepository.findByStudentAndAcademicYear(student, yearTermDTO.getAcademicYear())
					.orElseThrow(() -> new StudCareDataException("results not found"));
			for (TermResult termResult : termResults) {
				TermResultsDTO termResultsDTO = new TermResultsDTO();
				termResultsDTO.setTermNumber(termResult.getTermNumber());
				List<SubjectResultDTO> subjectResultDTOs = new ArrayList<>();
				List<SubjectResult> subjectResults = subjectResultRepository.findByTermResult(termResult);
				for (SubjectResult subjectResult : subjectResults) {
					SubjectResultDTO subjectResultDTO = new SubjectResultDTO();
					subjectResultDTO.setSubjectId(subjectResult.getSubject().getSubjectID());
					subjectResultDTO.setSubjectName(subjectResult.getSubject().getSubjectName());
					subjectResultDTO.setMarks(subjectResult.getMarks());
					subjectResultDTO.setGrade(subjectResult.getGrade());
					subjectResultDTO.setTeacherNote(subjectResult.getTeacherNote());
					subjectResultDTO.setTeacherName(subjectResult.getTeacher().getEmail());
					subjectResultDTOs.add(subjectResultDTO);
				}
				termResultsDTO.setSubjectResults(subjectResultDTOs);
				termResultsDTO.setClassRank(termResult.getClassRank());
				termResultsDTO.setTermNote(termResult.getClassTeacherNote());
				termResultsDTOs.add(termResultsDTO);
			}
			yearResultsDTO.setTermResults(termResultsDTOs);
			studentResultsDTO.setYearResults(Collections.singletonList(yearResultsDTO));
			studentResultsDTO.setSchoolClass(student.getSchoolClass());
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Student year results retrieved successfully");
			responseDTO.setData(Collections.singletonList(studentResultsDTO));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("StudentService.getStudentYearResults() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("StudentService.getStudentYearResults() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getStudentAllResults(String studentEmail) {
		log.info("StudentService.getStudentAllResults() initiated for student email: {}", studentEmail);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Student student = studentRepository.findByUser_Email(studentEmail).orElseThrow(() -> new StudCareDataException("Student not found"));
			StudentResultsDTO studentResultsDTO = new StudentResultsDTO();
			studentResultsDTO.setStudentId(student.getStudentId());
			studentResultsDTO.setStudentName(student.getUser().getUsername());
			List<YearResultsDTO> yearResultsDTOs = new ArrayList<>();
			List<TermResult> termResults = termResultRepository.findByStudent(student).orElseThrow(() -> new StudCareDataException("results not found"));
			for (TermResult termResult : termResults) {
				YearResultsDTO yearResultsDTO = yearResultsDTOs.stream().filter(yr -> yr.getAcademicYear().equals(termResult.getAcademicYear())).findFirst()
						.orElseGet(() -> {
							YearResultsDTO newYearResultsDTO = new YearResultsDTO();
							newYearResultsDTO.setAcademicYear(termResult.getAcademicYear());
							newYearResultsDTO.setTermResults(new ArrayList<>());
							yearResultsDTOs.add(newYearResultsDTO);
							return newYearResultsDTO;
						});
				TermResultsDTO termResultsDTO = new TermResultsDTO();
				termResultsDTO.setTermNumber(termResult.getTermNumber());
				List<SubjectResultDTO> subjectResultDTOs = new ArrayList<>();
				List<SubjectResult> subjectResults = subjectResultRepository.findByTermResult(termResult);
				for (SubjectResult subjectResult : subjectResults) {
					SubjectResultDTO subjectResultDTO = new SubjectResultDTO();
					subjectResultDTO.setSubjectId(subjectResult.getSubject().getSubjectID());
					subjectResultDTO.setSubjectName(subjectResult.getSubject().getSubjectName());
					subjectResultDTO.setMarks(subjectResult.getMarks());
					subjectResultDTO.setGrade(subjectResult.getGrade());
					subjectResultDTO.setTeacherNote(subjectResult.getTeacherNote());
					subjectResultDTO.setTeacherName(subjectResult.getTeacher().getEmail());
					subjectResultDTOs.add(subjectResultDTO);
				}
				termResultsDTO.setSubjectResults(subjectResultDTOs);
				termResultsDTO.setClassRank(termResult.getClassRank());
				termResultsDTO.setTermNote(termResult.getClassTeacherNote());
				yearResultsDTO.getTermResults().add(termResultsDTO);
				studentResultsDTO.setSchoolClass(student.getSchoolClass());
			}
			studentResultsDTO.setYearResults(yearResultsDTOs);
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Student results retrieved successfully");
			responseDTO.setData(Collections.singletonList(studentResultsDTO));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("StudentService.getStudentAllResults() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("StudentService.getStudentAllResults() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getMonthlyEvaluations(String studentId, MonthlyEvaluationsDTO requestDTO) {
		log.info("StudentService.getMonthlyEvaluations() initiated for student ID: {}", studentId);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Student student = studentRepository.findByUser_Email(studentId).orElseThrow(() -> new StudCareDataException("Student not found"));
			User hostelMaster = studentRepository.findHostelMasterByStudentId(student.getStudentId())
					.orElseThrow(() -> new StudCareDataException("Hostel Master not found"));
			List<MonthlyEvaluation> evaluations = monthlyEvaluationRepository.findByStudentAndEvaluationMonthIn(student, requestDTO.getMonths());
			UserDTO hostelMasterDTO = userAdapter.adapt(hostelMaster);
			MonthlyEvaluationResponseDTO monthlyEvaluationResponseDTO = monthlyEvaluationResponseAdapter.adapt(evaluations, hostelMasterDTO, student.getWard().getWardName());
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Evaluation details retrieved successfully");
			responseDTO.setData(Collections.singletonList(monthlyEvaluationResponseDTO));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareDataException exception) {
			log.error("StudentService.getMonthlyEvaluations() data error", exception);
			httpResponseData.setHttpStatus(HttpStatus.NOT_FOUND);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("StudentService.getMonthlyEvaluations() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> addClassTeacherNote(ClassTeacherNoteDTO noteDTO) {
		log.info("ClassService.addClassTeacherNote() initiated for student ID: {}", noteDTO.getStudent());
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Student student = studentRepository.findByUser_Email(noteDTO.getStudent()).orElseThrow(() -> new StudCareValidationException("Student not found"));
			User teacher = userRepository.findByEmail(noteDTO.getTeacher()).orElseThrow(() -> new StudCareValidationException("Teacher not found"));
			SchoolClass studentClass = student.getSchoolClass();
			if (!studentClass.getClassTeacher().equals(teacher)) {
				throw new StudCareValidationException("The provided teacher is not the class teacher for this student");
			}
			TermResult termResult = termResultRepository.findByStudentAndAcademicYearAndTermNumber(student, noteDTO.getAcademicYear(), noteDTO.getTerm())
					.orElseGet(() -> {
						TermResult newTermResult = new TermResult();
						newTermResult.setStudent(student);
						newTermResult.setAcademicYear(noteDTO.getAcademicYear());
						newTermResult.setTermNumber(noteDTO.getTerm());
						return newTermResult;
					});
			termResult.setClassTeacherNote(noteDTO.getClassTeacherNote());
			termResultRepository.save(termResult);
			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Class teacher note added successfully");
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("ClassService.addClassTeacherNote() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("ClassService.addClassTeacherNote() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	public ResponseEntity<Object> getStudentSuggestions(String studentEmail) {
		log.info("StudentService.getStudentSuggestions() initiated for student ID: {}", studentEmail);
		ResponseEntity<Object> responseEntity;
		HttpResponseData httpResponseData = new HttpResponseData();
		try {
			Student student = studentRepository.findByUser_Email(studentEmail).orElseThrow(() -> new StudCareDataException("Student not found"));
			Map<String, String> performanceData = new HashMap<>();
			Map<String, List<Double>> subjectMarks = new HashMap<>();
			List<TermResult> termResults = termResultRepository.findByStudent(student).orElseThrow(() -> new StudCareDataException("Results not found"));
			for (TermResult termResult : termResults) {
				for (SubjectResult subjectResult : termResult.getSubjectResults()) {
					String subjectName = subjectResult.getSubject().getSubjectName();
					double marks = Double.parseDouble(subjectResult.getMarks());
					subjectMarks.computeIfAbsent(subjectName, k -> new ArrayList<>()).add(marks);
				}
			}
			for (Map.Entry<String, List<Double>> entry : subjectMarks.entrySet()) {
				double average = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
				performanceData.put(entry.getKey(), String.format("%.2f", average));
			}
			List<MonthlyEvaluation> evaluations = monthlyEvaluationRepository.findByStudent(student);
			MonthlyEvaluationResponseDTO monthlyEvaluationResponseDTO = monthlyEvaluationResponseAdapter.adapt(evaluations, null, null);
			EnumMap<GradingScale, Integer> sportGradeCount = new EnumMap<>(GradingScale.class);
			EnumMap<GradingScale, Integer> extracurricularGradeCount = new EnumMap<>(GradingScale.class);
			for (List<MonthlyEvaluationData> monthlyData : monthlyEvaluationResponseDTO.getEvaluations().values()) {
				for (MonthlyEvaluationData data : monthlyData) {
					sportGradeCount.merge(data.getSportGrade(), 1, Integer::sum);
					extracurricularGradeCount.merge(data.getExtracurricularActivityGrade(), 1, Integer::sum);
				}
			}
			GradingScale mostFrequentSportGrade = getMostFrequentGrade(sportGradeCount);
			GradingScale mostFrequentExtracurricularGrade = getMostFrequentGrade(extracurricularGradeCount);
			performanceData.put("Sports", mostFrequentSportGrade.toString());
			performanceData.put("Extracurricular", mostFrequentExtracurricularGrade.toString());

			//ToDO call other backend to get the suggestion

			ResponseDTO responseDTO = new ResponseDTO();
			responseDTO.setResponseCode(Status.SUCCESS);
			responseDTO.setMessage("Student suggestions retrieved successfully");
			responseDTO.setData(Collections.singletonList("data need to be added here"));
			httpResponseData = responseAdapter.adapt(responseDTO);
			responseEntity = createResponseEntity(httpResponseData);
		} catch (StudCareValidationException exception) {
			log.error("StudentService.getStudentAllResults() validation error", exception);
			httpResponseData.setHttpStatus(HttpStatus.BAD_REQUEST);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		} catch (Exception exception) {
			log.error("StudentService.getStudentAllResults() an error occurred", exception);
			httpResponseData.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			httpResponseData.setResponseBody(exception.getMessage());
			responseEntity = createResponseEntity(httpResponseData);
		}
		return responseEntity;
	}

	private GradingScale getMostFrequentGrade(Map<GradingScale, Integer> gradeCount) {
		return gradeCount.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.orElse(null);
	}
}
