package com.studcare.data.jpa.adaptor;

import com.studcare.data.jpa.dto.SchoolClassDTO;
import com.studcare.data.jpa.dto.StudentDTO;
import com.studcare.data.jpa.dto.WardDTO;
import com.studcare.data.jpa.entity.SchoolClass;
import com.studcare.data.jpa.entity.Student;
import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.entity.Ward;
import com.studcare.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class StudentAdapter {
	@Autowired
	private UserAdapter userAdapter;
	@Autowired
	private SchoolClassAdapter schoolClassAdapter;
	@Autowired
	@Lazy
	private WardAdapter wardAdapter;

	public Student adapt(StudentDTO studentDTO) {
		Student student = new Student();
		if (!ObjectUtils.isEmpty(studentDTO.getStudentId())) {
			student.setStudentId(studentDTO.getStudentId());
		}
		User user = userAdapter.adapt(studentDTO.getUserDTO());
		student.setUser(user);
		if (!ObjectUtils.isEmpty(studentDTO.getSchoolClassDTO())) {
			SchoolClass schoolClass = schoolClassAdapter.adapt(studentDTO.getSchoolClassDTO());
			student.setSchoolClass(schoolClass);
		} else {
			student.setSchoolClass(null);
		}
		if (!ObjectUtils.isEmpty(studentDTO.getWardDTO())) {
			Ward ward = wardAdapter.adapt(studentDTO.getWardDTO());
			student.setWard(ward);
		} else {
			student.setWard(null);
		}

		return student;
	}

	public StudentDTO adapt(Student student) {
		StudentDTO studentDTO = new StudentDTO();
		if (!ObjectUtils.isEmpty(studentDTO.getStudentId())) {
			studentDTO.setStudentId(student.getStudentId());
		}
		studentDTO.setStudentId(student.getStudentId());
		UserDTO userDTO = userAdapter.adapt(student.getUser());
		studentDTO.setUserDTO(userDTO);
		if (!ObjectUtils.isEmpty(student.getSchoolClass())) {
			SchoolClassDTO schoolClassDTO = schoolClassAdapter.adapt(student.getSchoolClass());
			studentDTO.setSchoolClassDTO(schoolClassDTO);
		}
		if (!ObjectUtils.isEmpty(student.getWard())) {
			WardDTO wardDTO = wardAdapter.adapt(student.getWard());
			studentDTO.setWardDTO(wardDTO);
		}
		return studentDTO;
	}
}
