package com.studcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studcare.model.MonthlyEvaluationRequestDTO;
import com.studcare.service.HostelMasterService;
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
				HostelMasterController.class,
				HostelMasterService.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class HostelMasterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private HostelMasterService hostelMasterService;

	@Test
	void testGetWardDetails() throws Exception {
		String hostelMasterId = "hostelMasterId";
		when(hostelMasterService.getWardDetails(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/hostelmaster/{hostelMasterId}/ward", hostelMasterId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testAddMonthlyEvaluation() throws Exception {
		String hostelMasterId = "hostelMasterId";
		String studentId = "studentId";
		MonthlyEvaluationRequestDTO evaluationRequestDTO = new MonthlyEvaluationRequestDTO();
		evaluationRequestDTO.setExtraNote("Good");
		String requestBody = new ObjectMapper().writeValueAsString(evaluationRequestDTO);

		when(hostelMasterService.addMonthlyEvaluation(any(), any(), any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/hostelmaster/{hostelMasterId}/student/{studentId}/evaluation", hostelMasterId, studentId)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void testGetWardDetailsFailure() throws Exception {
		String hostelMasterId = "hostelMasterId";
		when(hostelMasterService.getWardDetails(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/hostelmaster/{hostelMasterId}/ward", hostelMasterId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testAddMonthlyEvaluationFailure() throws Exception {
		String hostelMasterId = "hostelMasterId";
		String studentId = "studentId";
		MonthlyEvaluationRequestDTO evaluationRequestDTO = new MonthlyEvaluationRequestDTO();
		evaluationRequestDTO.setExtraNote("Good");
		String requestBody = new ObjectMapper().writeValueAsString(evaluationRequestDTO);

		when(hostelMasterService.addMonthlyEvaluation(any(), any(), any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/hostelmaster/{hostelMasterId}/student/{studentId}/evaluation", hostelMasterId, studentId)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}
