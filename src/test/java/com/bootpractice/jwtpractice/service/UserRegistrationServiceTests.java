package com.bootpractice.jwtpractice.service;

import com.bootpractice.jwtpractice.dto.SignUpRequest;
import com.bootpractice.jwtpractice.entity.User;
import com.bootpractice.jwtpractice.exception.UserServiceExceptions;
import com.bootpractice.jwtpractice.utils.PasswordHasher;
import com.bootpractice.jwtpractice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.text.ParseException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@WithMockUser(username = "user" , password = "1234")
public class UserRegistrationServiceTests {

//	@Autowired
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
				.password("1234")
				.firstName("kim")
				.lastName("geunHwi")
				.email("test@test.com")
				.gender("Male")
				.phone("010-0000-0000")
				.legion("South Korea")
				.build();
	}



	@Test
	public void 유저가입테스트() throws Exception , UserServiceExceptions {
		//given
		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);
		when(userRepository.findByUsername("test@test.com")).thenReturn(Optional.empty());

		//when
		userRegistrationService.userRegistration(signUpRequest);
		User foundUser = userFindService.findByUsername("test@test.com");

		//then
		assertEquals(inputUser, foundUser, "Expected and found users should be the same: ");
	}


}