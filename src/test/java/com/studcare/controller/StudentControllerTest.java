package com.studcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.model.ClassTeacherNoteDTO;
import com.studcare.model.MonthlyEvaluationsDTO;
import com.studcare.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
		classes = StudentController.class
)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StudentService studentService;

	@Test
	void testGetTermResults() throws Exception {
		String student = "studentId";
		String requestBody = "{\"term\":\"1\"}";
		when(studentService.getStudentResults(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{student}/term/results", student)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetYearResults() throws Exception {
		String student = "studentId";
		String requestBody = "{\"year\":\"2023\"}";
		when(studentService.getStudentYearResults(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{student}/year/results", student)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetAllResults() throws Exception {
		String student = "studentId";
		when(studentService.getStudentAllResults(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{student}/all/results", student))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetMonthlyEvaluations() throws Exception {
		String studentId = "studentId";
		MonthlyEvaluationsDTO requestDTO = new MonthlyEvaluationsDTO();
		requestDTO.setMonths(Collections.singletonList("August"));
		String requestBody = new ObjectMapper().writeValueAsString(requestDTO);
		when(studentService.getMonthlyEvaluations(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{studentId}/evaluations", studentId)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testAddClassTeacherNote() throws Exception {
		ClassTeacherNoteDTO noteDTO = new ClassTeacherNoteDTO();
		noteDTO.setClassTeacherNote("Important note");
		String requestBody = new ObjectMapper().writeValueAsString(noteDTO);
		when(studentService.addClassTeacherNote(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/student/term/note")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void testGetTermResultsFailure() throws Exception {
		String student = "studentId";
		String requestBody = "{\"term\":\"1\"}";
		when(studentService.getStudentResults(any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{student}/term/results", student)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetYearResultsFailure() throws Exception {
		String student = "studentId";
		String requestBody = "{\"year\":\"2023\"}";
		when(studentService.getStudentYearResults(any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{student}/year/results", student)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetAllResultsFailure() throws Exception {
		String student = "studentId";
		when(studentService.getStudentAllResults(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{student}/all/results", student))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetMonthlyEvaluationsFailure() throws Exception {
		String studentId = "studentId";
		MonthlyEvaluationsDTO requestDTO = new MonthlyEvaluationsDTO();
		requestDTO.setMonths(Collections.singletonList("August"));
		String requestBody = new ObjectMapper().writeValueAsString(requestDTO);
		when(studentService.getMonthlyEvaluations(any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/student/{studentId}/evaluations", studentId)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddClassTeacherNoteFailure() throws Exception {
		ClassTeacherNoteDTO noteDTO = new ClassTeacherNoteDTO();
		noteDTO.setClassTeacherNote("Important note");
		String requestBody = new ObjectMapper().writeValueAsString(noteDTO);
		when(studentService.addClassTeacherNote(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/student/term/note")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}
