package com.bootpractice.jwtpractice.SecureLogin;

import com.bootpractice.jwtpractice.dto.CustomUserDetails;
import com.bootpractice.jwtpractice.entity.RefreshToken;
import com.bootpractice.jwtpractice.repository.RefreshTokenRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter   {
	private final AuthenticationManager authenticationManager;
	private final JWTTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public CustomLoginFilter(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.refreshTokenRepository = refreshTokenRepository;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		String username = obtainUsername(req);
		String password = obtainPassword(req);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password,null);

		return authenticationManager.authenticate(authRequest);
	}

	@Override
	public void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws java.io.IOException {
		System.out.println("Successful Authentication");
		CustomUserDetails customUserDetails = (CustomUserDetails)auth.getPrincipal();
		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		List<String> roles = authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String accessToken = jwtTokenProvider.createAccessToken(username, roles);
		if (accessToken == null) {
			System.err.println("Access token is null");
		} else {
			System.out.println("Access token generated: " + accessToken);
		}

		String refreshToken;

		Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findBySubject(username);
		if (existingRefreshToken.isPresent()) {
			String existingToken = existingRefreshToken.get().getRefreshToken();
			if (jwtTokenProvider.isRefreshTokenValid(existingToken)) {
				refreshToken = existingToken;
			} else {
				jwtTokenProvider.deleteRefreshToken(existingToken);
				refreshToken = jwtTokenProvider.createRefreshToken(username,roles);
			}
		} else {
			refreshToken = jwtTokenProvider.createRefreshToken(username,roles);
		}
		jwtTokenProvider.setAuthorizationHeaderForAccessToken(res, accessToken);
//		jwtTokenProvider.setAuthorizationHeaderForRefreshToken(res, refreshToken);

		// 클라이언트 측에서 리디렉션 처리
//		Cookie refreshTokenCookie = new Cookie("Refresh-Token", refreshToken);
//		refreshTokenCookie.setHttpOnly(true);
//		refreshTokenCookie.setSecure(true);
//		refreshTokenCookie.setPath("/");
//		refreshTokenCookie.setMaxAge(60 * 60 * 1000);


		res.setStatus(HttpServletResponse.SC_OK);

	}

	@Override
	public void unsuccessfulAuthentication(HttpServletRequest req,
	                                       HttpServletResponse res,
	                                       AuthenticationException failed) throws IOException, ServletException{
		System.out.println("Unsuccessful Authentication");
		res.setStatus(401);
	}


}
