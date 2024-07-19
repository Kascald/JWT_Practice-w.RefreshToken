//package com.onboarding.preonboarding.service;
//
//import dto.com.bootpractice.jwtpractice.SignUpRequest;
//import entity.com.bootpractice.jwtpractice.User;
//import repository.com.bootpractice.jwtpractice.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.util.AssertionErrors.assertTrue;
//
//
//@SpringBootTest
//@WithMockUser(username = "user" , password = "1234")
//public class UserServiceTests {
//
//	@Autowired
//	private UserService userService;
////	@Autowired
//	@MockBean
//	private UserRepository userRepository;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	public User createUserUser() {
//		return User.builder()
//				.username("test@test.com")
//				.password("1234")
//				.firstName("kim")
//				.lastName("geunHwi")
//				.email("test@test.com")
//				.gender("Male")
//				.phone("010-0000-0000")
//				.legion("South Korea")
//				.build();
//	}
//
//	@Test
//	public void 유저가입테스트() throws Exception {
//		//give
//		SignUpRequest signUpRequest =
//				new SignUpRequest("Kim" ,"geunHwi","test@test.com"
//				,"1234","2024/06/16","Male","010-0000-0000"
//				,"South Korea");
//
//		User user = signUpRequest.convertToUserEntity(passwordEncoder);
////		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
//		when(userRepository.findByUsername("test@test.com")).thenReturn(user);
//
//
//		//when
//		userService.userRegistration(signUpRequest);
////		User foundUser = userService.findByUsername2("test@test.com");
//
//		//then
//		boolean isEqual = user.equals(foundUser);
//		if (!isEqual) {
//			System.out.println("Expected: " + user);
//			System.out.println("Actual: " + foundUser);
//		}
//		assertTrue("User objects are not equal", isEqual);
//	}
//}