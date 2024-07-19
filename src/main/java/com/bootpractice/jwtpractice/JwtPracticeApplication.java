package com.bootpractice.jwtpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class JwtPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtPracticeApplication.class, args);
	}

}
