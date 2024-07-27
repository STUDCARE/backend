package com.studcare.data.jpa.adaptor;

import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.data.jpa.entity.SchoolClass;
import com.studcare.data.jpa.entity.User;
import com.studcare.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchoolClassAdapter {
	@Autowired
	private UserAdapter userAdapter;

	public SchoolClass adapt(SchoolClassDTO schoolClassDTO) {
		SchoolClass schoolClass = new SchoolClass();
		schoolClass.setClassName(schoolClassDTO.getClassName());
		User classTeacher = userAdapter.adapt(schoolClassDTO.getClassTeacher());
		schoolClass.setClassTeacher(classTeacher);
		schoolClass.setCreatedTimestamp(LocalDateTime.now());
		schoolClass.setModifiedTimestamp(LocalDateTime.now());
		return schoolClass;
	}

	public SchoolClassDTO adapt(SchoolClass schoolClass) {
		SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
		schoolClassDTO.setClassName(schoolClass.getClassName());
		UserDTO classTeacherDTO = userAdapter.adapt(schoolClass.getClassTeacher());
		schoolClassDTO.setClassTeacher(classTeacherDTO);
		return schoolClassDTO;
	}
}
