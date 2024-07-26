package com.bootpractice.jwtpractice.SecureLogin;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
public class TokenService {
	private final JWTTokenProvider jwtTokenProvider;
	public TokenService(JWTTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
	public Map<String ,String> refreshNewToken(String refresh) {

		String username = jwtTokenProvider.getUsernameFromToken(refresh);
		String email = jwtTokenProvider.getUserEmailFromToken(refresh);
		List<String> roleList = jwtTokenProvider.getRoleList(refresh);

		String newAccessToken = jwtTokenProvider.createAccessToken(username, roleList);
		String newRefreshToken = jwtTokenProvider.createRefreshToken(username, roleList);

		return Map.of("Access",newAccessToken , "Refresh",newRefreshToken);
	}

	public ResponseEntity<?> confirmExpired(String refresh) {
		if(refresh == null) {
			return new ResponseEntity<>("refresh token null", BAD_REQUEST);
		}
		try {
			jwtTokenProvider.isTokenExpiration(refresh);
		} catch (ExpiredJwtException e) {
			return new ResponseEntity<>("refresh token expired", BAD_REQUEST);
		}

		String category = jwtTokenProvider.tokenKindConfirm(refresh);

		if(!category.equals("Refresh")) {
			return new ResponseEntity<>("invalid refresh token", BAD_REQUEST);
		}
		return null;
	}

	public void newRefreshTokenSave(HttpServletResponse response , String newRefreshToken) {
		jwtTokenProvider.saveRefreshTokenInCookie(response, cookieGenerator("Refresh-Token", newRefreshToken));
	}

	private Cookie cookieGenerator(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(60 * 60 * 1000);
		cookie.setHttpOnly(true);

		return cookie;
	}


}
