package com.studcare.validator;

import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.ClassRequestDTO;
import com.studcare.template.GenericThrowableValidator;
import org.springframework.stereotype.Component;

import static com.studcare.util.CommonUtils.isEmpty;

@Component
public class ClassValidator implements GenericThrowableValidator<ClassRequestDTO> {
	@Override
	public void validate(ClassRequestDTO classRequestDTO) throws StudCareValidationException {
		if (!validateUserDetails(classRequestDTO.getSchoolClassDTO())) {
			throw new StudCareValidationException("ClassValidator.validate() error : one or more mandatory user details are missing.");
		}
	}

	private boolean validateUserDetails(SchoolClassDTO schoolClassDTO) {
		return !isEmpty(
				schoolClassDTO.getClassName(),
				schoolClassDTO.getClassTeacher().getEmail()
		);
	}
}