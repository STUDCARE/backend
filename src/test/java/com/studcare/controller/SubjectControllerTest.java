package com.studcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.model.HttpRequestData;
import com.studcare.service.SubjectService;
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
				SubjectController.class,
				SubjectService.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class SubjectControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubjectService subjectService;

	@Test
	void testCreateSubject() throws Exception {
		String requestBody = "{\"subjectName\":\"Subject A\"}";
		when(subjectService.createSubject(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/subject/create")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void testAddTeacher() throws Exception {
		String requestBody = "{\"teacherName\":\"Teacher A\"}";
		when(subjectService.addTeacher(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/subject/add/teacher")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetAllSubjects() throws Exception {
		when(subjectService.getAllSubjects()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/subject/all"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetSubjectsForTeacher() throws Exception {
		String teacher = "teacherId";
		when(subjectService.getSubjectsForTeacher(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/subject/teacher/{teacher}", teacher))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetTeachersForSubject() throws Exception {
		String subject = "subjectId";
		when(subjectService.getTeachersForSubject(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/subject/{subject}/teachers", subject))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testCreateSubjectFailure() throws Exception {
		String requestBody = "{\"subjectName\":\"Subject A\"}";
		when(subjectService.createSubject(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/subject/create")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddTeacherFailure() throws Exception {
		String requestBody = "{\"teacherName\":\"Teacher A\"}";
		when(subjectService.addTeacher(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/subject/add/teacher")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetAllSubjectsFailure() throws Exception {
		when(subjectService.getAllSubjects()).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/subject/all"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetSubjectsForTeacherFailure() throws Exception {
		String teacher = "teacherId";
		when(subjectService.getSubjectsForTeacher(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/subject/teacher/{teacher}", teacher))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetTeachersForSubjectFailure() throws Exception {
		String subject = "subjectId";
		when(subjectService.getTeachersForSubject(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/subject/{subject}/teachers", subject))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}
