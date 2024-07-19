package com.bootpractice.jwtpractice.service;

import com.bootpractice.jwtpractice.entity.User;
import com.bootpractice.jwtpractice.exception.UserServiceExceptions;
import com.bootpractice.jwtpractice.dto.SignUpRequest;
import com.bootpractice.jwtpractice.repository.UserRepository;
import com.bootpractice.jwtpractice.utils.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

import static com.bootpractice.jwtpractice.exception.UserServiceErrorCode.ALREADY_EXISTS_USERNAME;

@Service
public class UserRegistrationService implements UserServiceGeneral {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final PasswordHasher passwordHasher;
	private final UserRepository userRepository;
	private final UserFindService userFindService;

	public UserRegistrationService(PasswordHasher passwordEncoder, UserRepository userRepository, UserFindService userFindService) {
		this.passwordHasher = passwordEncoder;
		this.userRepository = userRepository;
		this.userFindService = userFindService;
	}

	public void userRegistration(SignUpRequest signUpRequest) throws ParseException {
		User inputUser = convertToUserEntity(signUpRequest);
		try {
			Boolean isExists = userRepository.existsByUsername(inputUser.getUsername());
			if(isExists) {throw new UserServiceExceptions(ALREADY_EXISTS_USERNAME);}
			saveUser(inputUser);
		} catch (UserServiceExceptions e) {
			e.printStackTrace();
		}
	}

	private User convertToUserEntity(SignUpRequest signUpRequest) throws ParseException {
		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);
		inputUser.setPassword(passwordHasher.hash(signUpRequest.getPassword()));
//		inputUser.setRoleList(List.of("USER"));
		inputUser.setRoleList(List.of("ADMIN"));
		return inputUser;
	}


	private void saveUser(User user) {
		userRepository.save(user);
	}


}
