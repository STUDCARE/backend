package com.studcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.model.SubjectResultsRequestDTO;
import com.studcare.service.TeacherService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
		classes = {
				TeacherController.class,
				TeacherService.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class TeacherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TeacherService teacherService;

	@Test
	void testGetTeacherSubjectsAndClasses() throws Exception {
		String teacherId = "teacherId";
		when(teacherService.getTeacherSubjectsAndClasses(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/{teacher}/subjects", teacherId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetClassTeacherDetails() throws Exception {
		String teacherId = "teacherId";
		when(teacherService.getClassTeacherDetails(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/{teacher}/class", teacherId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testAddSubjectResults() throws Exception {
		String teacherId = "teacherId";
		SubjectResultsRequestDTO resultsRequestDTO = new SubjectResultsRequestDTO();
		// Set properties on resultsRequestDTO if needed
		String requestBody = new ObjectMapper().writeValueAsString(resultsRequestDTO);
		when(teacherService.addSubjectResults(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/{teacher}/subject/result", teacherId)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetTeacherSubjectsAndClassesFailure() throws Exception {
		String teacherId = "teacherId";
		when(teacherService.getTeacherSubjectsAndClasses(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/{teacher}/subjects", teacherId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetClassTeacherDetailsFailure() throws Exception {
		String teacherId = "teacherId";
		when(teacherService.getClassTeacherDetails(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/teacher/{teacher}/class", teacherId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddSubjectResultsFailure() throws Exception {
		String teacherId = "teacherId";
		SubjectResultsRequestDTO resultsRequestDTO = new SubjectResultsRequestDTO();
		// Set properties on resultsRequestDTO if needed
		String requestBody = new ObjectMapper().writeValueAsString(resultsRequestDTO);
		when(teacherService.addSubjectResults(any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/teacher/{teacher}/subject/result", teacherId)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}
