package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/login")
	public String loadUI() {
		return "login";
	}

	@RequestMapping(value = "/register")
	public String register() {
		return "register";
	}

	@RequestMapping(value = { "/userTimesheet", "/" }, method = RequestMethod.GET)
	public String home() {
		return "userTimesheet";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/login?logout";
	}
	
	
	


}
