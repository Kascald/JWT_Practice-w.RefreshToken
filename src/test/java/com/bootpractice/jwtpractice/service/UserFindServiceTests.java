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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;



@SpringBootTest
class UserFindServiceTests {
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
    private List<User> testUserList;

	@BeforeEach
	public void setUp() throws ParseException {
		signUpRequest = getSignUpRequest();
		testUser = getCreateUserUser();
		testUserList = getTestUserList();
		testUserList.addAll(getTestFemaleUserList());
	}

	private SignUpRequest getSignUpRequest() throws ParseException {
		return new SignUpRequest("Kim" ,"geunHwi","test@test.com"
				,"1234","2024/06/16","Male","010-0000-0000"
				,"South Korea");
	}

	private User getCreateUserUser() {
		return User.builder()
				.username("test@test.com")
				.password(passwordHasher.hash("1234"))
				.firstName("Kim")
				.lastName("geunHwi")
				.email("test@test.com")
				.gender("Male")
				.phone("010-0000-0000")
				.legion("South Korea")
				.build();
	}

	private List<User> getTestUserList() {
		Random random = new Random();
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			User user = getCreateUserUser();

			String firstName = random.ints(97, 123)
					.limit(6)
					.collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
					.toString();

			String lastName= random.ints(97, 123)
					.limit(6)
					.collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
					.toString();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			userList.add(user);
		}
		return userList;
	}

	private List<User> getTestFemaleUserList() {
		Random random = new Random();
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			User user = getCreateUserUser();

			String firstName = random.ints(97, 123)
					.limit(6)
					.collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
					.toString();

			String lastName= random.ints(97, 123)
					.limit(6)
					.collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
					.toString();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setLegion("USA");
			user.setGender("Female");
			userList.add(user);
		}
		return userList;
	}

	@Test
	public void 랜덤인트테스트() {
		Random random = new Random();
		String randomkey = random.ints(33, 127)
				.limit(50)
				.collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
				.toString();
		logger.info("random Key :  {}", randomkey);

		randomkey = passwordHasher.hash(randomkey);
		logger.info("random Key hashed:  {}", randomkey);

		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedKey = encoder.encode(randomkey.getBytes());
		logger.info("random Key.getByte() base64 encoded:  {}", encodedKey);

	}

	@Test
	public void 유저조회테스트() throws UserServiceExceptions {
		//given
		when(userRepository.findByUsername("test@test.com")).thenReturn(Optional.of(testUser));

		//when
		User foundUser = userFindService.findByUsername("test@test.com");
		logger.info("Found User info : {} ",foundUser.toString());

		//then
		assertEquals(testUser, foundUser, "Expected and found users should be the same: ");
	}

	@Test
	public void 유저조회테스트_실명기반조회() throws ParseException, UserServiceExceptions {
		//given
		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);
		when(userRepository.findByFirstNameAndLastName("Kim","geunHwi")).thenReturn(Optional.of(inputUser));

		//when
		User foundUser = userFindService.findByUserRealName("Kim","geunHwi");
		logger.info("Found User info : {} ",foundUser.toString());

		//then
		assertEquals(inputUser, foundUser, "User Real Name is Same!! : ");
	}

	@Test
	public void 유저조회테스트_이메일기반조회() throws ParseException, UserServiceExceptions {
		//given
		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);
		when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(inputUser));

		//when
		User foundUser = userFindService.findByEmail("test@test.com");
		logger.info("Found User info : {} ",foundUser.toString());

		//then
		assertEquals(inputUser.getEmail(), foundUser.getEmail(), "User Email is Same!! : ");
	}

//	@Test
//	public void 유저조회테스트_역할기반조회() throws ParseException, UserServiceExceptions {}

	@Test
	public void 유저조회테스트_legion기반_조회() throws ParseException, UserServiceExceptions {
		//given
		List<User> users = testUserList;
		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);
		users.add(inputUser);
		logger.info("users --> {}", users.size()); //expected 9

		List<User> koreanUsers = users.stream()
				.filter(user -> "South Korea".equals(user.getLegion()))
				.toList();
		logger.info("koreanUsers --> {}", koreanUsers.size());

		when(userRepository.findByLegion("South Korea"))
				.thenReturn(Optional.of(koreanUsers));

		//when
		List<User> foundUsers = userFindService.findByLegion("South Korea");
		logger.info("Found Users info : {} ",foundUsers.size()); //expected 6

		//then
		assertThat(foundUsers).isEqualTo(koreanUsers);
	}

	@Test
	public void 유저조회테스트_gender기반_조회() throws ParseException, UserServiceExceptions {
		//given
		List<User> users = testUserList;
		User inputUser = signUpRequest.convertToUserEntity(passwordHasher);
		users.add(inputUser);
		logger.info("users number --> {}", users.size()); // expected 5 + 3 + 1 = 9

		List<User> FemaleUsers = users.stream()
				.filter(user -> "Female".equals(user.getGender()))
				.toList();
		logger.info("FemaleUsers --> {}", FemaleUsers.size());

		when(userRepository.findByGender("Female"))
				.thenReturn(Optional.of(FemaleUsers));

		//when
		List<User> foundUsers = userFindService.findByGender("Female");
		logger.info("Found Users number info : {} ",foundUsers.size() );

		//then
		assertThat(foundUsers).isEqualTo(FemaleUsers);
	}

}