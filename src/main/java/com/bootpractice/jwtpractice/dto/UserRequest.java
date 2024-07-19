package com.bootpractice.jwtpractice.dto;

import com.bootpractice.jwtpractice.entity.User;
import com.bootpractice.jwtpractice.utils.PasswordHasher;

import java.text.ParseException;
import java.util.Date;

public interface UserRequest {
	User convertToUserEntity(PasswordHasher passwordHasher) throws ParseException;

	Date birthDayToDate(String inputDate) throws ParseException;
}
