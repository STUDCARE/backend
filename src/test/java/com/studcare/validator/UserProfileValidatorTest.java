package com.studcare.validator;

import com.studcare.exception.StudCareValidationException;
import com.studcare.model.UserProfileRequestDTO;
import com.studcare.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {UserProfileValidator.class})
class UserProfileValidatorTest {

	private UserProfileValidator userProfileValidator;

	@BeforeEach
	void setUp() {
		userProfileValidator = new UserProfileValidator();
	}

	@Test
	void validateSuccess() {
		UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();
		userProfileRequestDTO.setUserEmail("user@gmail.com");

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(userProfileRequestDTO.getUserEmail()))
					.thenReturn(false);

			assertDoesNotThrow(() -> userProfileValidator.validate(userProfileRequestDTO));
		}
	}

	@Test
	void validateFailure() {
		UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();
		userProfileRequestDTO.setUserEmail("");

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(userProfileRequestDTO.getUserEmail()))
					.thenReturn(true);

			assertThrows(StudCareValidationException.class, () -> userProfileValidator.validate(userProfileRequestDTO));
		}
	}
}
