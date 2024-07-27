package com.studcare.validator;

import com.studcare.exception.StudCareValidationException;
import com.studcare.model.UserDTO;
import com.studcare.model.UserDeletionRequestDTO;
import com.studcare.template.GenericThrowableValidator;
import org.springframework.stereotype.Component;

import static com.studcare.util.CommonUtils.isEmpty;

@Component
public class UserDeletionValidator implements GenericThrowableValidator<UserDeletionRequestDTO> {
	@Override
	public void validate(UserDeletionRequestDTO userDeletionRequestDTO) throws StudCareValidationException {
		if (!validateUserDetails(userDeletionRequestDTO.getUserEmail())) {
			throw new StudCareValidationException("UserDeletionValidator.validate() error : one or more mandatory user details are missing.");
		}
	}

	private boolean validateUserDetails(String email) {
		return !isEmpty(email);
	}
}
