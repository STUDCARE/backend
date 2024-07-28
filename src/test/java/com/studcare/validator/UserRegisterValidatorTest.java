package com.studcare.validator;

import com.studcare.data.jpa.entity.UserRole;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.UserDTO;
import com.studcare.model.UserRegisterRequestDTO;
import com.studcare.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {UserRegisterValidator.class})
class UserRegisterValidatorTest {

	private UserRegisterValidator userRegisterValidator;

	@BeforeEach
	void setUp() {
		userRegisterValidator = new UserRegisterValidator();
	}

	@Test
	void validateSuccess() {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("user@gmail.com");
		userDTO.setPassword("password");
		userDTO.setUsername("username");
		userDTO.setRole(UserRole.STUDENT);
		userDTO.setClassTeacher(true);

		UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
		userRegisterRequestDTO.setUserDTO(userDTO);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(
							userDTO.getEmail(),
							userDTO.getPassword(),
							userDTO.getUsername(),
							userDTO.getRole().name(),
							String.valueOf(userDTO.isClassTeacher())))
					.thenReturn(false);

			assertDoesNotThrow(() -> userRegisterValidator.validate(userRegisterRequestDTO));
		}
	}

	@Test
	void validateFailure() {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("");
		userDTO.setPassword("password");
		userDTO.setUsername("username");
		userDTO.setRole(UserRole.STUDENT);
		userDTO.setClassTeacher(true);

		UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
		userRegisterRequestDTO.setUserDTO(userDTO);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(
							userDTO.getEmail(),
							userDTO.getPassword(),
							userDTO.getUsername(),
							userDTO.getRole().name(),
							String.valueOf(userDTO.isClassTeacher())))
					.thenReturn(true);

			assertThrows(StudCareValidationException.class, () -> userRegisterValidator.validate(userRegisterRequestDTO));
		}
	}
}
