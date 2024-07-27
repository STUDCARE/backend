package com.studcare;

import com.studcare.data.jpa.entity.User;
import com.studcare.data.jpa.entity.UserRole;
import com.studcare.data.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserInitializer implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		Optional<User> existingUser = userRepository.findByEmail("admin@gmail.com");
		if (existingUser.isPresent()) {
			return;
		}
		User user = new User();
		user.setEmail("admin@gmail.com");
		user.setPassword(passwordEncoder.encode("123"));
		user.setRole(UserRole.ADMINISTRATOR);
		user.setUsername("Admin User");
		user.setIsClassTeacher(false);
		userRepository.save(user);
		log.info("Admin user successfully created");
	}
}
