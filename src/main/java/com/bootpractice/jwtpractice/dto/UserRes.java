package com.bootpractice.jwtpractice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter @Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRes {
	private String status;
	private String message;
	private String requestURI;
	private String username;
	private String firstname;
	private String lastname;
}
