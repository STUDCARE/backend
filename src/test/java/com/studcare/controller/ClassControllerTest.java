package com.studcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.model.YearTermDTO;
import com.studcare.service.ClassService;
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
				ClassController.class,
				ClassService.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class ClassControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClassService classService;

	@Test
	void testCreateClass() throws Exception {
		String requestBody = "{\"className\":\"Class A\"}";
		when(classService.createClass(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/create")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void testAddStudents() throws Exception {
		String className = "Class A";
		String requestBody = "[\"student1\", \"student2\"]";
		when(classService.addStudents(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/add/students", className)
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testAddSubjects() throws Exception {
		String className = "Class A";
		String requestBody = "[\"subject1\", \"subject2\"]";
		when(classService.addSubjectsToClass(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/add/subjects", className)
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testAddTeacherToClassSubject() throws Exception {
		String teacher = "teacherId";
		String subject = "subjectId";
		String className = "Class A";
		when(classService.addTeacherToClassSubject(any(), any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/{subject}/assign/{teacher}", className, subject, teacher))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetStudentsInClass() throws Exception {
		String className = "Class A";
		when(classService.getStudentsInClass(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{className}/students", className))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetClassesForTeacherAndSubject() throws Exception {
		String teacherId = "teacherId";
		String subjectId = "subjectId";
		when(classService.getClassesForTeacherAndSubject(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{teacherId}/{subjectId}", teacherId, subjectId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetClassDetails() throws Exception {
		String className = "Class A";
		when(classService.getClassDetails(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{className}/details", className))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetClassResults() throws Exception {
		String className = "Class A";
		String requestBody = "{\"academicYear\":\"2023\",\"term\":\"1\"}";
		when(classService.getClassResults(any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{className}/results", className)
						.header("Header1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testCalculateClassResults() throws Exception {
		String className = "ClassA";
		YearTermDTO yearTermDTO = new YearTermDTO();
		yearTermDTO.setAcademicYear("2023");
		yearTermDTO.setTerm("1");
		String requestBody = new ObjectMapper().writeValueAsString(yearTermDTO);
		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/term/result", className)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	@Test
	void testCreateClassFailure() throws Exception {
		String requestBody = "{\"className\":\"Class A\"}";
		when(classService.createClass(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/create")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddStudentsFailure() throws Exception {
		String className = "Class A";
		String requestBody = "[\"student1\", \"student2\"]";
		when(classService.addStudents(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/add/students", className)
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddSubjectsFailure() throws Exception {
		String className = "Class A";
		String requestBody = "[\"subject1\", \"subject2\"]";
		when(classService.addSubjectsToClass(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/add/subjects", className)
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddTeacherToClassSubjectFailure() throws Exception {
		String teacher = "teacherId";
		String subject = "subjectId";
		String className = "Class A";
		when(classService.addTeacherToClassSubject(any(), any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/{subject}/assign/{teacher}", className, subject, teacher))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetStudentsInClassFailure() throws Exception {
		String className = "Class A";
		when(classService.getStudentsInClass(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{className}/students", className))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetClassesForTeacherAndSubjectFailure() throws Exception {
		String teacherId = "teacherId";
		String subjectId = "subjectId";
		when(classService.getClassesForTeacherAndSubject(any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{teacherId}/{subjectId}", teacherId, subjectId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetClassDetailsFailure() throws Exception {
		String className = "Class A";
		when(classService.getClassDetails(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{className}/details", className))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetClassResultsFailure() throws Exception {
		String className = "Class A";
		String requestBody = "{\"academicYear\":\"2023\",\"term\":\"1\"}";
		when(classService.getClassResults(any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/class/{className}/results", className)
						.header("Header1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testCalculateClassResultsFailure() throws Exception {
		String className = "ClassA";
		YearTermDTO yearTermDTO = new YearTermDTO();
		yearTermDTO.setAcademicYear("2023");
		yearTermDTO.setTerm("1");

		when(classService.calculateClassResults(any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		String requestBody = new ObjectMapper().writeValueAsString(yearTermDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/class/{className}/term/result", className)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}