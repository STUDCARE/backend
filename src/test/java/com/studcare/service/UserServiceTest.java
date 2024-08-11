package com.studcare.service;

import com.studcare.adapter.GetUsersAdapter;
import com.studcare.adapter.ResponseAdapter;
import com.studcare.constants.Status;
import com.studcare.data.jpa.adaptor.UserAdapter;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.entity.UserRole;
import com.studcare.data.jpa.repository.StudentRepository;
import com.studcare.data.jpa.repository.UserRepository;
import com.studcare.security.JwtService;
import com.studcare.exception.StudCareDataException;
import com.studcare.exception.StudCareRuntimeException;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.AllUsersRequestDTO;
import com.studcare.model.HttpRequestData;
import com.studcare.model.HttpResponseData;
import com.studcare.model.LogoutRequestDTO;
import com.studcare.model.LogoutResponseDTO;
import com.studcare.model.ResponseDTO;
import com.studcare.model.UserDTO;
import com.studcare.model.UserDeletionResponseDTO;
import com.studcare.model.UserProfileResponseDTO;
import com.studcare.model.UserRegisterResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserService.class})
class UserServiceTest {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserAdapter userAdapter;

	@MockBean
	private ResponseAdapter responseAdapter;

	@MockBean
	private GetUsersAdapter getUsersAdapter;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private StudentRepository studentRepository;


	@Test
	void registerUserSuccess() throws StudCareValidationException {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("user@gmail.com");
		userDTO.setPassword("password");
		userDTO.setUsername("username");
		userDTO.setRole(UserRole.STUDENT);
		User user = new User();
		user.setEmail("user@gmail.com");
		user.setRole(UserRole.STUDENT);

		when(userAdapter.adapt(userDTO)).thenReturn(user);
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

		UserRegisterResponseDTO response = userService.register(userDTO);

		verify(userRepository, times(1)).save(user);
		verify(studentRepository, times(1)).save(any(Student.class));
		assertEquals(Status.SUCCESS, response.getResponseCode());
		assertEquals("User account created successfully for user@gmail.com", response.getMessage());
	}

	@Test
	void registerUserAlreadyExists() throws StudCareValidationException {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("user@gmail.com");
		User user = new User();
		user.setEmail("user@gmail.com");

		when(userAdapter.adapt(userDTO)).thenReturn(user);
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		UserRegisterResponseDTO response = userService.register(userDTO);

		assertEquals(Status.FAILURE, response.getResponseCode());
		assertEquals("User already exists with email: user@gmail.com", response.getMessage());
	}

	@Test
	void registerUserDatabaseError() {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("user@gmail.com");
		User user = new User();
		user.setEmail("user@gmail.com");

		when(userAdapter.adapt(userDTO)).thenReturn(user);
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
		doThrow(new RuntimeException("Database error")).when(userRepository).save(user);

		assertThrows(StudCareDataException.class, () -> userService.register(userDTO));
	}

	@Test
	void deleteUserSuccess() throws StudCareValidationException {
		String email = "user@gmail.com";
		User user = new User();
		user.setEmail(email);
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		UserDeletionResponseDTO response = userService.delete(email);

		verify(userRepository, times(1)).delete(user);
		assertEquals(Status.SUCCESS, response.getResponseCode());
		assertEquals("User account deleted successfully for " + email, response.getMessage());
	}

	@Test
	void deleteUserNotFound() throws StudCareValidationException {
		String email = "user@gmail.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		UserDeletionResponseDTO response = userService.delete(email);

		assertEquals(Status.FAILURE, response.getResponseCode());
		assertEquals("User not found with email: " + email, response.getMessage());
	}

	@Test
	void deleteUserDatabaseError() {
		String email = "user@gmail.com";
		User user = new User();
		user.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		doThrow(new RuntimeException("Database error")).when(userRepository).delete(user);

		assertThrows(StudCareDataException.class, () -> userService.delete(email));
	}

	@Test
	void getUserProfileSuccess() throws StudCareValidationException {
		String email = "user@gmail.com";
		User user = new User();
		user.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(userAdapter.adapt(user)).thenReturn(new UserDTO());

		UserProfileResponseDTO response = userService.getUserProfile(email);

		assertEquals(Status.SUCCESS, response.getResponseCode());
		assertEquals("User profile retrieved successfully", response.getMessage());
		assertNotNull(response.getUserDTO());
	}

	@Test
	void getUserProfileNotFound() throws StudCareValidationException {
		String email = "user@gmail.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		UserProfileResponseDTO response = userService.getUserProfile(email);

		assertEquals(Status.FAILURE, response.getResponseCode());
		assertEquals("No user with email: " + email, response.getMessage());
		assertNull(response.getUserDTO());
	}

	@Test
	void logoutSuccess() throws StudCareValidationException {
		String email = "user@gmail.com";
		String token = "token";
		User user = new User();
		user.setEmail(email);
		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setEmail(email);
		logoutRequestDTO.setHeaders(Map.of("authorization", "Bearer " + token));

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(jwtService.invalidateToken(user, token)).thenReturn(true);

		LogoutResponseDTO response = userService.logout(logoutRequestDTO);

		assertEquals(Status.SUCCESS, response.getResponseCode());
		assertEquals("User logged out successfully", response.getMessage());
	}

	@Test
	void logoutFailure() throws StudCareValidationException {
		String email = "user@gmail.com";
		String token = "token";
		User user = new User();
		user.setEmail(email);
		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setEmail(email);
		logoutRequestDTO.setHeaders(Map.of("authorization", "Bearer " + token));

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(jwtService.invalidateToken(user, token)).thenReturn(false);

		LogoutResponseDTO response = userService.logout(logoutRequestDTO);

		assertEquals(Status.FAILURE, response.getResponseCode());
	}


	@Test
	void logoutUserNotFound() {
		String email = "user@gmail.com";
		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		assertThrows(StudCareValidationException.class, () -> userService.logout(logoutRequestDTO));
	}

	@Test
	void logoutUserError() throws UsernameNotFoundException {
		String email = "user@gmail.com";
		String token = "token";
		User user = new User();
		user.setEmail(email);
		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setEmail(email);
		logoutRequestDTO.setHeaders(Map.of("authorization", "Bearer " + token));

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(jwtService.invalidateToken(user, token)).thenThrow(UsernameNotFoundException.class);

		LogoutResponseDTO response = userService.logout(logoutRequestDTO);

		assertEquals(Status.FAILURE, response.getResponseCode());
	}

	@Test
	void logoutUserErrorInvalidToken() throws UsernameNotFoundException {
		String email = "user@gmail.com";
		String token = "token";
		User user = new User();
		user.setEmail(email);
		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setEmail(email);
		logoutRequestDTO.setHeaders(Map.of("authorization", "invalid " + token));

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(jwtService.invalidateToken(user, token)).thenThrow(UsernameNotFoundException.class);

		LogoutResponseDTO response = userService.logout(logoutRequestDTO);

		assertEquals(Status.FAILURE, response.getResponseCode());
	}

	@Test
	void getAllUsersByRoleSuccess() throws StudCareValidationException {
		HttpRequestData httpRequestData = new HttpRequestData();
		HttpResponseData httpResponseData = new HttpResponseData();
		httpResponseData.setHttpStatus(HttpStatus.OK);
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("user@gmail.com");
		userDTO.setRole(UserRole.STUDENT);
		AllUsersRequestDTO allUsersRequestDTO = new AllUsersRequestDTO();
		allUsersRequestDTO.setUserRole(UserRole.STUDENT.name());
		List<User> users = List.of(new User(), new User());

		when(getUsersAdapter.adapt(httpRequestData)).thenReturn(allUsersRequestDTO);
		when(userRepository.findByRole(UserRole.STUDENT)).thenReturn(Optional.of(users));
		when(userAdapter.adapt(any(User.class))).thenReturn(new UserDTO());
		when(responseAdapter.adapt(any())).thenReturn(httpResponseData);

		ResponseEntity<Object> response = userService.getAllUsersByRole(httpRequestData);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void getAllUsersByRoleValidationException() throws StudCareValidationException {
		HttpRequestData httpRequestData = new HttpRequestData();

		when(getUsersAdapter.adapt(httpRequestData)).thenThrow(new StudCareValidationException("Validation error"));

		ResponseEntity<Object> response = userService.getAllUsersByRole(httpRequestData);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}


	@Test
	void getAllUsersByRoleRuntimeException() {
		HttpRequestData httpRequestData = new HttpRequestData();
		AllUsersRequestDTO allUsersRequestDTO = new AllUsersRequestDTO();
		allUsersRequestDTO.setUserRole(UserRole.STUDENT.name());

		when(getUsersAdapter.adapt(httpRequestData)).thenReturn(allUsersRequestDTO);
		when(userRepository.findByRole(UserRole.STUDENT)).thenThrow(new StudCareRuntimeException("Runtime error"));

		ResponseEntity<Object> response = userService.getAllUsersByRole(httpRequestData);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void getAllUsersByRoleGenericException() throws StudCareDataException {
		HttpRequestData httpRequestData = new HttpRequestData();

		when(getUsersAdapter.adapt(httpRequestData)).thenThrow(new StudCareDataException("database error"));

		ResponseEntity<Object> response = userService.getAllUsersByRole(httpRequestData);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void getUsersSuccess() throws StudCareValidationException {
		String userRole = UserRole.STUDENT.name();
		List<User> users = List.of(new User(), new User());

		when(userRepository.findByRole(UserRole.STUDENT)).thenReturn(Optional.of(users));
		when(userAdapter.adapt(any(User.class))).thenReturn(new UserDTO());

		ResponseDTO response = userService.getUsers(userRole);

		assertEquals(Status.SUCCESS, response.getResponseCode());
		assertEquals("User profile retrieved successfully", response.getMessage());
		assertNotNull(response.getData());
	}

	@Test
	void getUsersInvalidRole() throws StudCareValidationException {
		String userRole = "INVALID_ROLE";

		ResponseDTO response = userService.getUsers(userRole);

		assertEquals(Status.FAILURE, response.getResponseCode());
		assertEquals("Invalid user role: " + userRole, response.getMessage());
	}

	@Test
	void getUsersNoUsersFound() throws StudCareValidationException {
		String userRole = UserRole.STUDENT.name();

		when(userRepository.findByRole(UserRole.STUDENT)).thenReturn(Optional.empty());

		ResponseDTO response = userService.getUsers(userRole);

		assertEquals(Status.FAILURE, response.getResponseCode());
		assertEquals("No user with role: " + userRole, response.getMessage());
	}
}
