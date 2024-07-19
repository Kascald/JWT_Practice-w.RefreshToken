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


	public String hash(String beforeEncodePassword) {
		return passwordEncoder.encode(beforeEncodePassword);
	}

	public boolean match(String rawPassword, String hashedPassword) {
		return passwordEncoder.matches(rawPassword, hashedPassword);
	}

}
