package com.studcare.controller;

import com.studcare.service.AccountService;
import com.studcare.service.UserService;
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
				UserController.class,
				AccountService.class,
				UserService.class
		}
)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;

	@MockBean
	private UserService userService;

	@Test
	void testLogoutUser() throws Exception {
		String email = "user@example.com";
		when(accountService.userLogout(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.post("/user/logout/{email}", email)
						.header("Header1", "Value1")
						.param("param1", "Value1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testViewUserProfile() throws Exception {
		String email = "user@example.com";
		when(accountService.viewUserProfile(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/user/profile/{email}", email)
						.header("Header1", "Value1")
						.param("param1", "Value1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetAllUsersByRole() throws Exception {
		String requestBody = "{\"role\":\"admin\"}";
		when(userService.getAllUsersByRole(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(MockMvcRequestBuilders.get("/user/all")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testLogoutUserFailure() throws Exception {
		String email = "user@example.com";
		when(accountService.userLogout(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.post("/user/logout/{email}", email)
						.header("Header1", "Value1")
						.param("param1", "Value1"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testViewUserProfileFailure() throws Exception {
		String email = "user@example.com";
		when(accountService.viewUserProfile(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/user/profile/{email}", email)
						.header("Header1", "Value1")
						.param("param1", "Value1"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	void testGetAllUsersByRoleFailure() throws Exception {
		String requestBody = "{\"role\":\"admin\"}";
		when(userService.getAllUsersByRole(any())).thenThrow(new RuntimeException("Simulated exception"));

		mockMvc.perform(MockMvcRequestBuilders.get("/user/all")
						.header("Header1", "Value1")
						.param("param1", "Value1")
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}
