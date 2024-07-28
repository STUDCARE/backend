package com.studcare.service;

import com.studcare.adapter.UserDeletionRequestAdapter;
import com.studcare.adapter.UserDeletionResponseAdapter;
import com.studcare.adapter.UserLogoutRequestAdapter;
import com.studcare.adapter.UserLogoutResponseAdapter;
import com.studcare.adapter.UserProfileRequestAdapter;
import com.studcare.adapter.UserProfileResponseAdapter;
import com.studcare.adapter.UserRegisterRequestAdapter;
import com.studcare.adapter.UserRegisterResponseAdapter;
import com.studcare.exception.StudCareDataException;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.model.LogoutRequestDTO;
import com.studcare.model.LogoutResponseDTO;
import com.studcare.model.UserDTO;
import com.studcare.model.UserDeletionRequestDTO;
import com.studcare.model.UserDeletionResponseDTO;
import com.studcare.model.UserProfileRequestDTO;
import com.studcare.model.UserProfileResponseDTO;
import com.studcare.model.UserRegisterRequestDTO;
import com.studcare.model.UserRegisterResponseDTO;
import com.studcare.validator.UserDeletionValidator;
import com.studcare.validator.UserLogoutValidator;
import com.studcare.validator.UserProfileValidator;
import com.studcare.validator.UserRegisterValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(
		classes = {
				AccountService.class
		}
)
class AccountServiceTest {

	@Autowired
	private AccountService accountService;

	@MockBean
	private UserRegisterRequestAdapter userRegisterRequestAdapter;
	@MockBean
	private UserRegisterResponseAdapter userRegisterResponseAdapter;
	@MockBean
	private UserDeletionRequestAdapter userDeletionRequestAdapter;
	@MockBean
	private UserDeletionResponseAdapter userDeletionResponseAdapter;
	@MockBean
	private UserProfileRequestAdapter userProfileRequestAdapter;
	@MockBean
	private UserProfileResponseAdapter userProfileResponseAdapter;
	@MockBean
	private UserLogoutRequestAdapter userLogoutRequestAdapter;
	@MockBean
	private UserLogoutResponseAdapter userLogoutResponseAdapter;
	@MockBean
	private UserRegisterValidator userRegisterValidator;
	@MockBean
	private UserDeletionValidator userDeletionValidator;
	@MockBean
	private UserProfileValidator userProfileValidator;
	@MockBean
	private UserLogoutValidator userLogoutValidator;
	@MockBean
	private UserService userService;

	@BeforeEach
	public void setup() {
		doNothing().when(userRegisterValidator).validate(Mockito.any());
		doNothing().when(userDeletionValidator).validate(Mockito.any());
		doNothing().when(userProfileValidator).validate(Mockito.any());
		doNothing().when(userLogoutValidator).validate(Mockito.any());
	}

	@Test
	void testCreateUser() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("user@gmail.com");
		UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
		userRegisterRequestDTO.setUserDTO(userDTO);
		UserRegisterResponseDTO userRegisterResponseDTO = Mockito.mock(UserRegisterResponseDTO.class);

		Mockito.when(userRegisterRequestAdapter.adapt(httpRequestData)).thenReturn(userRegisterRequestDTO);
		Mockito.when(userService.register(userRegisterRequestDTO.getUserDTO())).thenReturn(userRegisterResponseDTO);
		Mockito.when(userRegisterResponseAdapter.adapt(userRegisterResponseDTO)).thenReturn(httpResponseData);

		ResponseEntity<Object> response = accountService.createUser(httpRequestData);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testCreateUserValidationException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userRegisterRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareValidationException("Validation error"));
		ResponseEntity<Object> response = accountService.createUser(httpRequestData);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testCreateUserRuntimeException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userRegisterRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareRuntimeException("Runtime error"));
		ResponseEntity<Object> response = accountService.createUser(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testCreateUserGenericException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userRegisterRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareDataException("Generic error"));
		ResponseEntity<Object> response = accountService.createUser(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testDeleteUser() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);
		UserDeletionRequestDTO userDeletionRequestDTO = new UserDeletionRequestDTO();
		userDeletionRequestDTO.setUserEmail("user@gmail.com");
		UserDeletionResponseDTO userDeletionResponseDTO = Mockito.mock(UserDeletionResponseDTO.class);

		Mockito.when(userDeletionRequestAdapter.adapt(httpRequestData)).thenReturn(userDeletionRequestDTO);
		Mockito.when(userService.delete(userDeletionRequestDTO.getUserEmail())).thenReturn(userDeletionResponseDTO);
		Mockito.when(userDeletionResponseAdapter.adapt(userDeletionResponseDTO)).thenReturn(httpResponseData);

		ResponseEntity<Object> response = accountService.deleteUser(httpRequestData);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testDeleteUserValidationException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userDeletionRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareValidationException("Validation error"));
		ResponseEntity<Object> response = accountService.deleteUser(httpRequestData);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testDeleteUserRuntimeException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userDeletionRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareRuntimeException("Runtime error"));
		ResponseEntity<Object> response = accountService.deleteUser(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testDeleteUserGenericException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userDeletionRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareDataException("Generic error"));
		ResponseEntity<Object> response = accountService.deleteUser(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testViewUserProfile() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);
		UserProfileRequestDTO userProfileRequestDTO = Mockito.mock(UserProfileRequestDTO.class);
		userProfileRequestDTO.setUserEmail("user@gmail.com");
		UserProfileResponseDTO userProfileResponseDTO = Mockito.mock(UserProfileResponseDTO.class);

		Mockito.when(userProfileRequestAdapter.adapt(httpRequestData)).thenReturn(userProfileRequestDTO);
		Mockito.when(userService.getUserProfile(userProfileRequestDTO.getUserEmail())).thenReturn(userProfileResponseDTO);
		Mockito.when(userProfileResponseAdapter.adapt(userProfileResponseDTO)).thenReturn(httpResponseData);

		ResponseEntity<Object> response = accountService.viewUserProfile(httpRequestData);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testViewUserProfileValidationException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userProfileRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareValidationException("Validation error"));
		ResponseEntity<Object> response = accountService.viewUserProfile(httpRequestData);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testViewUserProfileRuntimeException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userProfileRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareRuntimeException("Runtime error"));
		ResponseEntity<Object> response = accountService.viewUserProfile(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testViewUserProfileGenericException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userProfileRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareDataException("Generic error"));
		ResponseEntity<Object> response = accountService.viewUserProfile(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testUserLogout() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);
		LogoutRequestDTO logoutRequestDTO = Mockito.mock(LogoutRequestDTO.class);
		LogoutResponseDTO logoutResponseDTO = Mockito.mock(LogoutResponseDTO.class);

		Mockito.when(userLogoutRequestAdapter.adapt(httpRequestData)).thenReturn(logoutRequestDTO);
		Mockito.when(userService.logout(logoutRequestDTO)).thenReturn(logoutResponseDTO);
		Mockito.when(userLogoutResponseAdapter.adapt(logoutResponseDTO)).thenReturn(httpResponseData);

		ResponseEntity<Object> response = accountService.userLogout(httpRequestData);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testUserLogoutValidationException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userLogoutRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareValidationException("Validation error"));
		ResponseEntity<Object> response = accountService.userLogout(httpRequestData);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void testUserLogoutRuntimeException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userLogoutRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareRuntimeException("Runtime error"));
		ResponseEntity<Object> response = accountService.userLogout(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void testUserLogoutGenericException() {
		HttpRequestData httpRequestData = Mockito.mock(HttpRequestData.class);
		Mockito.when(userLogoutRequestAdapter.adapt(httpRequestData)).thenThrow(new StudCareDataException("Generic error"));
		ResponseEntity<Object> response = accountService.userLogout(httpRequestData);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
}
