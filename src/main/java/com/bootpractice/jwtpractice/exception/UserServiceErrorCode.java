package com.bootpractice.jwtpractice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum UserServiceErrorCode {
	ALREADY_EXISTS_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 정보의 유저를 찾을 수 없습니다."),
	PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST,"올바르지 않은 비밀번호 입니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST,"패스워드가 일치하지 않습니다."),;
	private final HttpStatus httpStatus;
	private final String message;
}
