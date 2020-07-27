package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author Marwan
 *
 */
@Controller
public class LoginController {
	/**
	 * for login process
	 * 
	 * @return login.html
	 */
	@RequestMapping(value = "/login")
	public String loadUI() {
		return "login";
	}

	/**
	 * create new User
	 * 
	 * @return register.html
	 */
	@RequestMapping(value = "/register")
	public String register() {
		return "register";
	}

	/**
	 * User Log times (in/out)
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/userTimesheet", "/" }, method = RequestMethod.GET)
	public String home() {
		return "userTimesheet";
	}

	/**
	 * redirect to login page after logout Success
	 * 
	 * @return
	 */
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/login?logout";
	}

}
