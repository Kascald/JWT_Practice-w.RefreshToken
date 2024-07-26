package com.bootpractice.jwtpractice.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
	private final PasswordEncoder passwordEncoder;

	public PasswordHasher(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}


	public String hash(String password) { //beforeEncodePassword
		return passwordEncoder.encode(password);
	}

	public boolean match(String password, String hashedPassword) {//rawPassword , hashedPassword
		return passwordEncoder.matches(password, hashedPassword);
	}

}
