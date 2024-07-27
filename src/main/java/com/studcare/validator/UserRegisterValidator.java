package com.studcare.validator;

import com.studcare.exception.StudCareValidationException;
import com.studcare.model.UserDTO;
import com.studcare.model.UserRegisterRequestDTO;
import com.studcare.template.GenericThrowableValidator;
import org.springframework.stereotype.Component;

import static com.studcare.util.CommonUtils.isEmpty;

@Component
public class UserRegisterValidator implements GenericThrowableValidator<UserRegisterRequestDTO> {
	@Override
	public void validate(UserRegisterRequestDTO userRegisterRequestDTO) throws StudCareValidationException {
		if (!validateUserDetails(userRegisterRequestDTO.getUserDTO())) {
			throw new StudCareValidationException("UserRegisterValidator.validate() error : one or more mandatory user details are missing.");
		}
	}

	private boolean validateUserDetails(UserDTO userDTO) {
		return !isEmpty(
				userDTO.getEmail(),
				userDTO.getPassword(),
				userDTO.getUsername(),
				userDTO.getRole().name(),
				String.valueOf(userDTO.isClassTeacher())
		);
	}
}
