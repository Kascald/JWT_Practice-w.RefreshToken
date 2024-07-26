package com.bootpractice.jwtpractice.controller;


import com.bootpractice.jwtpractice.SecureLogin.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

		//토큰 삭제 , 블랙리스트 등록
		tokenService.deleteRefreshToken(refresh);
		tokenService.saveInBlackList(refresh);
		//새 토큰 저장
		tokenService.saveRefreshTokenInDB(newRefresh);

		//새 토큰 헤더설정, 새 리프레시 토큰 쿠키저장
		response.setHeader("Authorization", newAccess);
		tokenService.newRefreshTokenSaveInCookie(response, newRefresh);

		return new ResponseEntity<>(OK);
	}

	@GetMapping("/cleanUpExpired")
	public void tokenCleaning() {
		tokenService.moveExpiredTokenToBlackList();
	}
}
