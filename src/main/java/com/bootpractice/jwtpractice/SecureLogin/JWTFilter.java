package com.bootpractice.jwtpractice.SecureLogin;

import com.bootpractice.jwtpractice.dto.CustomUserDetails;
import com.bootpractice.jwtpractice.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {
	private final JWTTokenProvider jwtTokenProvider;
	private final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
	public JWTFilter(JWTTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//Header Find
		String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {
			logger.info("token NULL");
			filterChain.doFilter(request,response);

			return;
		}

		logger.info("authorization now");
		//Authorization Bearer token get
		String token = authorization.split(" ")[1];

		try {
			jwtTokenProvider.isTokenExpiration(authorization);
		} catch (ExpiredJwtException e) {
			PrintWriter writer = response.getWriter();
			writer.print("Access Token Expired");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String category = jwtTokenProvider.tokenKindConfirm(authorization);

		if(!category.equals("Access")) {
			PrintWriter writer = response.getWriter();
			writer.print("Access Token Invalid");

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}


		String username = jwtTokenProvider.getUsernameFromToken(token);
		List<String> roleList = jwtTokenProvider.getRoleList(token);
		logger.info("token sub : {} , role : {}",username , roleList);

		//		UserDTO thisUser = new UserDTO();
		User thisUser = new User();
		thisUser.setUsername(username);
		thisUser.setRoleList(roleList);
		thisUser.setPassword("temp");

		CustomUserDetails customUserDetails = new CustomUserDetails(thisUser);
		logger.info("CustomUserDetails created: " + customUserDetails);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
		logger.info("Authentication token created: " + authToken);

		SecurityContextHolder.getContext().setAuthentication(authToken);
		logger.info("Security context updated with authentication token");

		// 추가 로그: SecurityContextHolder의 Authentication 객체 확인
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.info("SecurityContextHolder Authentication: " + auth);
			logger.info("Principal: " + auth.getPrincipal());
			logger.info("Authorities: " + auth.getAuthorities());
		} else {
			logger.info("SecurityContextHolder Authentication is null");
		}

		filterChain.doFilter(request, response);
	}


}
