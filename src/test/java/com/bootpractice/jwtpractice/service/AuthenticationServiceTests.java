package com.bootpractice.jwtpractice.service;

import com.bootpractice.jwtpractice.dto.SignUpRequest;
import com.bootpractice.jwtpractice.entity.User;
import com.bootpractice.jwtpractice.exception.UserServiceExceptions;
import com.bootpractice.jwtpractice.utils.PasswordHasher;
import com.bootpractice.jwtpractice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;


@SpringBootTest
class AuthenticationServiceTests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserRegistrationService userRegistrationService;

	@Autowired
	private UserFindService userFindService;

	@Autowired
	private PasswordHasher passwordHasher;

	private SignUpRequest signUpRequest;
	private User testUser;

	@BeforeEach
	public void setUp() throws ParseException {
		signUpRequest = getSignUpRequest();
		testUser = getCreateUserUser();
	}

	public SignUpRequest getSignUpRequest() throws ParseException {
		return new SignUpRequest("Kim" ,"geunHwi","test@test.com"
				,"1234","2024/06/16","Male","010-0000-0000"
				,"South Korea");
	}

	public User getCreateUserUser() {
		return User.builder()
				.username("test@test.com")
				.password(passwordHasher.hash("1234"))
				.firstName("kim")
				.lastName("geunHwi")
				.email("test@test.com")
				.gender("Male")
				.phone("010-0000-0000")
				.legion("South Korea")
				.build();
	}




	@Test
	public void 유저_패스워드_매칭확인() throws ParseException {
		//give
		User modifyUser = signUpRequest.convertToUserEntity(passwordHasher);
		when(userRepository.findByUsername("test@test.com")).thenReturn(Optional.of(testUser));
		User foundUser = null;
		boolean isMatch = false;

		//when
		try {
			foundUser = userFindService.findByUsername("test@test.com");
			logger.info("Found User info : {} ",foundUser.toString());
			isMatch = passwordHasher.match(signUpRequest.getPassword(), foundUser.getPassword());

		} catch (UserServiceExceptions e) {
			logger.info("Found User info : Null ");
		}

		//then
		assertTrue("User Password Matche : {}", isMatch);
	}

}