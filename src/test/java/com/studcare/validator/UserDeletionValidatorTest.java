package com.studcare.validator;

import com.studcare.exception.StudCareValidationException;
import com.studcare.model.UserDeletionRequestDTO;
import com.studcare.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {UserDeletionValidator.class})
class UserDeletionValidatorTest {

	private UserDeletionValidator userDeletionValidator;

	@BeforeEach
	void setUp() {
		userDeletionValidator = new UserDeletionValidator();
	}

	@Test
	void validateSuccess() {
		String userEmail = "user@gmail.com";

		UserDeletionRequestDTO userDeletionRequestDTO = new UserDeletionRequestDTO();
		userDeletionRequestDTO.setUserEmail(userEmail);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(userEmail))
					.thenReturn(false);

			assertDoesNotThrow(() -> userDeletionValidator.validate(userDeletionRequestDTO));
		}
	}

	@Test
	void validateFailure() {
		String userEmail = "";

		UserDeletionRequestDTO userDeletionRequestDTO = new UserDeletionRequestDTO();
		userDeletionRequestDTO.setUserEmail(userEmail);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(userEmail))
					.thenReturn(true);

			assertThrows(StudCareValidationException.class, () -> userDeletionValidator.validate(userDeletionRequestDTO));
		}
	}
}
