package com.bootpractice.jwtpractice.controller;


import com.bootpractice.jwtpractice.SecureLogin.JWTTokenProvider;
import com.bootpractice.jwtpractice.SecureLogin.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Controller
@ResponseBody
public class reissueController {

	private final TokenService tokenService;

	public reissueController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	//	private final JWTTokenProvider jwtTokenProvider;
//	public reissueController(JWTTokenProvider jwtTokenProvider) {
//		this.jwtTokenProvider = jwtTokenProvider;
//	}

	@PostMapping("/reissue")
	public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
		String refresh = null;
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if (cookie.getName().equals("Refresh-Token")) {
				refresh = cookie.getValue();
			}
		}

		tokenService.confirmExpired(refresh);

		String newAccess = tokenService.refreshNewToken(refresh).get("Access");
		String newRefresh = tokenService.refreshNewToken(refresh).get("refresh");

		response.setHeader("Authorization", newAccess);
		tokenService.newRefreshTokenSave(response,newRefresh);

		return new ResponseEntity<>(OK);
	}
}
