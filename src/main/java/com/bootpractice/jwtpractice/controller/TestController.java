package com.bootpractice.jwtpractice.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@Controller
@ResponseBody
@RequestMapping("/roleTest")
public class TestController {
	@GetMapping("/test")
	public String index() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return "HELLO " + name;
	}

	@GetMapping("/userInfo")
	public String infoView() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iter = authorities.iterator();
		GrantedAuthority auth = iter.next();
		String role = auth.getAuthority();


		return "Username  : " + username + ", role : " + role;
	}
}
