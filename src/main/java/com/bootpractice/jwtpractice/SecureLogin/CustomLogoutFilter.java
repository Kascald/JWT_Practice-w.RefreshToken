package com.bootpractice.jwtpractice.SecureLogin;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {
	private final JWTTokenProvider jwtTokenProvider;
	private final TokenService tokenService;

	public CustomLogoutFilter(JWTTokenProvider jwtTokenProvider, TokenService tokenService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.tokenService = tokenService;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		doFilter((HttpServletRequest)servletRequest , (HttpServletResponse)servletResponse ,filterChain);
	}

	private void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		if (!requestUri.matches("^\\/lgoout$")) {
			filterChain.doFilter(req, res);
			return;
		}
		String requestMethod = req.getMethod();
		if(!requestMethod.equals("POST")) {
			filterChain.doFilter(req,res);
			return;
		}
		String refresh = null;

		Cookie[] cookies = req.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("Refresh-Token")) {
				refresh = cookie.getValue();
			}
		}

		String category = jwtTokenProvider.tokenKindConfirm(refresh);
		if (!category.equals("Refresh")) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		Boolean isExist = tokenService.isExistsInDB(refresh);
		if (!isExist) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		tokenService.deleteRefreshToken(refresh);

		Cookie cookie = new Cookie("Refresh-Token",null);
		cookie.setMaxAge(0);
		cookie.setPath("/");

		jwtTokenProvider.saveRefreshTokenInCookie(res,cookie);
		res.setStatus(HttpServletResponse.SC_OK);

	}
}
