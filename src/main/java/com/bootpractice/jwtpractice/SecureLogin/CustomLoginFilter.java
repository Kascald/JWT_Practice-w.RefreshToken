package com.bootpractice.jwtpractice.SecureLogin;

import com.bootpractice.jwtpractice.dto.CustomUserDetails;
import com.bootpractice.jwtpractice.entity.RefreshToken;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
	private final TokenService tokenService;
	public CustomLoginFilter(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, TokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.tokenService = tokenService;
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

		Optional<RefreshToken> existingRefreshToken = tokenService.getRefreshToken(username);
		if (existingRefreshToken.isPresent()) {
			String existingToken = existingRefreshToken.get().getRefreshToken();
			if (tokenService.isRefreshTokenValid(existingToken)) {
				refreshToken = existingToken;
			} else {
				tokenService.deleteRefreshToken(existingToken);
				refreshToken = jwtTokenProvider.createRefreshToken(username,roles);
			}
		} else {
			refreshToken = jwtTokenProvider.createRefreshToken(username,roles);
		}
		jwtTokenProvider.setAuthorizationHeaderForAccessToken(res, accessToken);
//		jwtTokenProvider.setAuthorizationHeaderForRefreshToken(res, refreshToken);
		tokenService.newRefreshTokenSaveInCookie(res, refreshToken);

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
