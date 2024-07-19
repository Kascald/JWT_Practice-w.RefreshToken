package com.bootpractice.jwtpractice.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	public String signup() {
		return "user/signup";
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
