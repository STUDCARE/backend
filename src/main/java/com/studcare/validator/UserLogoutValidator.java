package com.studcare.validator;

import com.studcare.exception.StudCareValidationException;
import com.studcare.model.LogoutRequestDTO;
import com.studcare.model.UserDTO;
import com.studcare.template.GenericThrowableValidator;
import org.springframework.stereotype.Component;

import static com.studcare.util.CommonUtils.isEmpty;

@Component
public class UserLogoutValidator implements GenericThrowableValidator<LogoutRequestDTO> {
	@Override
	public void validate(LogoutRequestDTO logoutRequestDTO) throws StudCareValidationException {
		if (!validateUserDetails(logoutRequestDTO.getEmail())) {
			throw new StudCareValidationException("UserDeletionValidator.validate() error : one or more mandatory user details are missing.");
		}
	}

	private boolean validateUserDetails(String email) {
		return !isEmpty(email);
	}
}