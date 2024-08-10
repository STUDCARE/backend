package com.studcare.validator;

import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.exception.StudCareValidationException;
import com.studcare.model.ClassRequestDTO;
import com.studcare.model.UserDTO;
import com.studcare.util.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {ClassValidator.class})
class ClassValidatorTest {

	private ClassValidator classValidator;

	@BeforeEach
	void setUp() {
		classValidator = new ClassValidator();
	}

	@Test
	void validateSuccess() {
		UserDTO classTeacher = new UserDTO();
		classTeacher.setEmail("teacher@gmail.com");

		SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
		schoolClassDTO.setClassName("Grade 1");
		schoolClassDTO.setClassTeacher(classTeacher);

		ClassRequestDTO classRequestDTO = new ClassRequestDTO();
		classRequestDTO.setSchoolClassDTO(schoolClassDTO);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(
							schoolClassDTO.getClassName(),
							schoolClassDTO.getClassTeacher().getEmail()))
					.thenReturn(false);

			assertDoesNotThrow(() -> classValidator.validate(classRequestDTO));
		}
	}

	@Test
	void validateFailure() {
		UserDTO classTeacher = new UserDTO();
		classTeacher.setEmail("");

		SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
		schoolClassDTO.setClassName("Grade 1");
		schoolClassDTO.setClassTeacher(classTeacher);

		ClassRequestDTO classRequestDTO = new ClassRequestDTO();
		classRequestDTO.setSchoolClassDTO(schoolClassDTO);

		try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
			mockedCommonUtils.when(() -> CommonUtils.isEmpty(
							schoolClassDTO.getClassName(),
							schoolClassDTO.getClassTeacher().getEmail()))
					.thenReturn(true);

			assertThrows(StudCareValidationException.class, () -> classValidator.validate(classRequestDTO));
		}
	}
}
