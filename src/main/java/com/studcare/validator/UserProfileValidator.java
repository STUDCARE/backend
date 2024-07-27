package com.studcare.validator;

import com.studcare.exception.StudCareValidationException;
import com.studcare.model.UserDTO;
import com.studcare.model.UserProfileRequestDTO;
import com.studcare.template.GenericThrowableValidator;
import org.springframework.stereotype.Component;

import static com.studcare.util.CommonUtils.isEmpty;

@Component
public class UserProfileValidator implements GenericThrowableValidator<UserProfileRequestDTO> {
	@Override
	public void validate(UserProfileRequestDTO userProfileRequestDTO) throws StudCareValidationException {
		if (!validateUserDetails(userProfileRequestDTO.getUserEmail())) {
			throw new StudCareValidationException("UserProfileValidator.validate() error : user email is required.");
		}
	}

	private boolean validateUserDetails(String userEmail) {
		return !isEmpty(userEmail);
	}
}