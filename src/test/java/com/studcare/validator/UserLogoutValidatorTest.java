package com.studcare.validator;

import com.studcare.exception.StudCareValidationException;
import com.studcare.model.LogoutRequestDTO;
import com.studcare.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {UserLogoutValidator.class})
class UserLogoutValidatorTest {

	private UserLogoutValidator userLogoutValidator;

	@BeforeEach
	void setUp() {
		userLogoutValidator = new UserLogoutValidator();
	}

	@Test
	void validateSuccess() {
		String email = "user@gmail.com";

		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setEmail(email);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(email))
					.thenReturn(false);

			assertDoesNotThrow(() -> userLogoutValidator.validate(logoutRequestDTO));
		}
	}

	@Test
	void validateFailure() {
		String email = "";

		LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO();
		logoutRequestDTO.setEmail(email);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(email))
					.thenReturn(true);

			assertThrows(StudCareValidationException.class, () -> userLogoutValidator.validate(logoutRequestDTO));
		}
	}
}
