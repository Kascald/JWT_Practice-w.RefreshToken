package com.bootpractice.jwtpractice.controller;


import com.bootpractice.jwtpractice.dto.SignUpRequest;
import com.bootpractice.jwtpractice.dto.UserRes;
import com.bootpractice.jwtpractice.exception.UserServiceExceptions;
import com.bootpractice.jwtpractice.service.AuthenticationService;
import com.bootpractice.jwtpractice.service.UserAuthenticationService;
import com.bootpractice.jwtpractice.service.UserRegistrationService;
import com.bootpractice.jwtpractice.utils.ResponseCoverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping(value = "/user/api")
public class UserController {
	private final AuthenticationService authenticationService;
	private final UserAuthenticationService userAuthenticationService;
	private final UserRegistrationService userRegistrationService;
	private final ResponseCoverter responseCoverter;

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController(AuthenticationService authenticationService,
	                      UserAuthenticationService userAuthenticationService, UserRegistrationService userRegistrationService, ResponseCoverter responseCoverter) {
		this.authenticationService = authenticationService;
		this.userAuthenticationService = userAuthenticationService;
		this.userRegistrationService = userRegistrationService;
		this.responseCoverter = responseCoverter;
	}

	@PostMapping("/signup")
	public ResponseEntity<UserRes> registUser(@RequestBody SignUpRequest signUpRequest) throws ParseException {
		ResponseEntity<UserRes> resResponseEntity;

		try {
			// service .registration (userDto ) ;
			userRegistrationService.userRegistration(signUpRequest);

			UserRes userRes = new UserRes("Ok","Done","/user/api/signup",
			                              signUpRequest.getEmail(),signUpRequest.getFirstName(),signUpRequest.getLastName());
			resResponseEntity= ResponseEntity.status(HttpStatus.OK).body(userRes);
			logger.info("응답 성공");
			return resResponseEntity;

		} catch (ParseException e) {
			// response of service to ResponseEntity
			UserRes userRes = new UserRes("Parse_error","Retry it","/user/api/signup",
			                              signUpRequest.getEmail(),signUpRequest.getFirstName(),signUpRequest.getLastName());
			resResponseEntity= ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userRes);
			return resResponseEntity;
		}

	}

	@ExceptionHandler(UserServiceExceptions.class)
	public ResponseEntity<String> handleUserServiceException(UserServiceExceptions ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // HTTP 409 Conflict
	}

//	@PostMapping("/login")
//	public ResponseEntity<UserRes> loginUser(@RequestBody LoginRequest loginRequest) {
//		ResponseEntity<UserRes> resResponseEntity;
//		try {
//			// service .registration (userDto ) ;
//			userRegistrationService.userRegistration(loginRequest);
//
//			UserRes userRes = new UserRes("Ok","Done","/user/api/signup",
//			                              loginRequest.getEmail(),loginRequest.getFirstName(),loginRequest.getLastName());
//			resResponseEntity= ResponseEntity.status(HttpStatus.OK).body(userRes);
//			logger.info("응답 성공");
//			return resResponseEntity;
//
//		} catch (ParseException e) {
//			// response of service to ResponseEntity
//			UserRes userRes = new UserRes("Parse_error","Retry it","/user/api/signup",
//			                              loginRequest.getEmail(),loginRequest.getFirstName(),loginRequest.getLastName());
//			resResponseEntity= ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userRes);
//			return resResponseEntity;
//		}
//	}


}
