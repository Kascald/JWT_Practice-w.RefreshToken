package com.bootpractice.jwtpractice.controller;


import com.bootpractice.jwtpractice.dto.SignUpRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

@Controller
public class MenuController {
	@GetMapping("/")
	public String index() {
		return "index";
	}
	@RequestMapping(value = "item/list" , method = RequestMethod.GET)
	public String list() {
		return "item/list";
	}

	@RequestMapping(value = "user/signup" , method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute("SignupRequest",new SignUpRequest());
		return "user/signup";
	}
	@RequestMapping("/result")
	public String login(HttpSession session, Model model) {
		String jwt = (String) session.getAttribute("Authorization");
		String jwtRefresh = (String) session.getAttribute("Refresh-Token");

		model.addAttribute("Authorization", jwt);
		model.addAttribute("Refresh-Token", jwtRefresh);

		return "user/result";
	}

	@RequestMapping(value = "user/login" , method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	@RequestMapping(value = "user/logout" , method = RequestMethod.GET)
	public String logout() {
		return "user/logout";
	}

}
