package com.studcare.controller;

import com.studcare.service.WardService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(
		classes = {
				WardController.class,
				WardService.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class WardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WardService wardService;

	@Test
	void testCreateWard() throws Exception {
		String requestBody = "{\"wardName\":\"Ward A\"}";
		when(wardService.createWard(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/ward/create")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void testAddStudents() throws Exception {
		String wardName = "Ward A";
		String requestBody = "[\"student1\", \"student2\"]";
		when(wardService.addStudents(anyString(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/ward/{wardName}/add/students", wardName)
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetStudentsInWard() throws Exception {
		String wardName = "Ward A";
		when(wardService.getStudentsInWard(anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/ward/{wardName}/students", wardName))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetWardDetails() throws Exception {
		String wardName = "Ward A";
		when(wardService.getWardDetails(anyString())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/ward/{wardName}/details", wardName))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testCreateWardFailure() throws Exception {
		String requestBody = "{\"wardName\":\"Ward A\"}";
		when(wardService.createWard(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/ward/create")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddStudentsFailure() throws Exception {
		String wardName = "Ward A";
		String requestBody = "[\"student1\", \"student2\"]";
		when(wardService.addStudents(anyString(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/ward/{wardName}/add/students", wardName)
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetStudentsInWardFailure() throws Exception {
		String wardName = "Ward A";
		when(wardService.getStudentsInWard(anyString())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/ward/{wardName}/students", wardName))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetWardDetailsFailure() throws Exception {
		String wardName = "Ward A";
		when(wardService.getWardDetails(anyString())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/ward/{wardName}/details", wardName))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetAllWards() throws Exception {
		when(wardService.getAllWards()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/ward/all"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetAllWardsFailure() throws Exception {
		when(wardService.getAllWards()).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/ward/all"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}
