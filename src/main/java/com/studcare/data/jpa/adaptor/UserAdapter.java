package com.studcare.data.jpa.adaptor;

import com.studcare.data.jpa.entity.User;
import com.studcare.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Component
public class UserAdapter{
	@Autowired
	private PasswordEncoder passwordEncoder;

	public User adapt(UserDTO userDTO) {
		User user = new User();
		if (!ObjectUtils.isEmpty(userDTO.getUserId())) {
			user.setUserID(userDTO.getUserId());
		}
		user.setEmail(userDTO.getEmail());
		user.setUsername(userDTO.getUsername());
		if (!ObjectUtils.isEmpty(userDTO.getPassword())) {
			user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		} else {
			user.setPassword(null);
		}
		user.setRole(userDTO.getRole());
		user.setIsClassTeacher(userDTO.isClassTeacher());
		user.setCreatedTimestamp(userDTO.getCreatedTimestamp());
		user.setModifiedTimestamp(LocalDateTime.now());
		return user;
	}

	public UserDTO adapt(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(user.getUserID());
		userDTO.setEmail(user.getEmail());
		userDTO.setUsername(user.getUsername());
		userDTO.setPassword(user.getPassword());
		userDTO.setRole(user.getRole());
		userDTO.setClassTeacher(user.getIsClassTeacher());
		userDTO.setCreatedTimestamp(user.getCreatedTimestamp());
		userDTO.setModifiedTimestamp(LocalDateTime.now());
		return userDTO;
	}
}
