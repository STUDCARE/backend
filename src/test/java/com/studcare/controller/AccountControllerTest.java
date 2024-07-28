package com.studcare.controller;

import com.studcare.service.AccountService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
		classes = {
				AccountController.class,
				AccountService.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@Test
	void testCreateUser() throws Exception {
		String requestBody = "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}";
		when(accountService.createUser(any())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

		mockMvc.perform(MockMvcRequestBuilders.post("/account/create").header("Header1", "Value1").param("param1", "Value1").content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void testCreateUserFailure() throws Exception {
		String requestBody = "{\"name\":\"John Doe\",\"email\":\"john@example.com\"}";
		when(accountService.createUser(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/account/create").header("Header1", "Value1").param("param1", "Value1").content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testDeleteUser() throws Exception {
		String email = "john@example.com";
		when(accountService.deleteUser(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/account/delete/{email}", email).header("Header1", "Value1").param("param1", "Value1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testDeleteUserFailure() throws Exception {
		String email = "john@example.com";
		when(accountService.deleteUser(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/account/delete/{email}", email).header("Header1", "Value1").param("param1", "Value1"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}