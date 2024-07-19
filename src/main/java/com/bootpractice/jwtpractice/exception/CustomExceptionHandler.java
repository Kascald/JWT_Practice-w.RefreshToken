package com.bootpractice.jwtpractice.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(UserServiceExceptions.class)
	protected ResponseEntity<DefaultErrorResponse> handleUserServiceException(
			UserServiceExceptions userServiceExceptions) {

		return DefaultErrorResponse.toDefaultErrorResponse(userServiceExceptions.getErrorCode());
	}
}
