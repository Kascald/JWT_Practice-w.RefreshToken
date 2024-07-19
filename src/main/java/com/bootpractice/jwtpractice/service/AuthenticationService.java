package com.bootpractice.jwtpractice.service;

import com.bootpractice.jwtpractice.SecureLogin.JWTTokenProvider;
import com.bootpractice.jwtpractice.dto.UserDTO;
import com.bootpractice.jwtpractice.exception.UserServiceExceptions;
import com.bootpractice.jwtpractice.utils.PasswordHasher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	private final UserFindService userFindService;
	private final PasswordHasher passwordHasher;
	private final JWTTokenProvider jwtTokenProvider;
	private final UserAuthenticationService userAuthenticationService;

	public AuthenticationService(UserFindService userFindService, PasswordHasher passwordHasher, JWTTokenProvider jwtTokenProvider, UserAuthenticationService userAuthenticationService) {
		this.userFindService = userFindService;
		this.passwordHasher = passwordHasher;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userAuthenticationService = userAuthenticationService;
	}

	//유저 패스워드 매칭확인
	public boolean isMatchPassword(String username, String rawPassword) {
		UserDTO foundUser = null;
//		User foundUser = null;
		try {
			foundUser =  userFindService.findUserDTOByUsername(username);
//			foundUser =  userFindService.findByUsername(username);
			return passwordHasher.match(rawPassword ,foundUser.getPassword());
		} catch (UserServiceExceptions e) {
			return false;
		}
	}

	// JWT 에서 인증 정보 조회
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userAuthenticationService.loadUserByUsername(
				jwtTokenProvider.getUserEmailFromToken(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String resolveAccessToken(HttpServletRequest request) {
		if (request.getHeader("authorization") != null)
			return request.getHeader("authorization").substring(7);
		return null;
	}

	public String resolveRefreshToken(HttpServletRequest request) {
		if (request.getHeader("refreshToken") != null)
			return request.getHeader("refreshToken").substring(7);
		return null;
	}
}
