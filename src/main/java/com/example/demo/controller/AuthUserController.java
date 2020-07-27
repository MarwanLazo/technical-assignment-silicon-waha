package com.example.demo.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.log.AuthUserAlreadyException;
import com.example.demo.model.AuthUser;
import com.example.demo.service.AuthUserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Marwan
 *
 */
@CrossOrigin(allowedHeaders = "*")
@Log4j2
@AllArgsConstructor
@RestController
public class AuthUserController {
	private final AuthUserService service;

	/**
	 * 
	 * @return all users
	 */
	@GetMapping(value = "/user/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AuthUser> getAll() {
		List<AuthUser> authUser = service.getAll();
		log.info(authUser);
		return authUser;
	}

	/**
	 * 
	 * @param authUser
	 * @return created User
	 */
	@PostMapping(value = "/user/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public AuthUser create(@RequestBody AuthUser authUser) {

		log.info(String.format("AuthUser create(%s)", authUser));
		if (authUser == null || authUser.getEmail() == null) {
			authUser = service.create(authUser);
		}
		AuthUser user = service.getUserByEmail(authUser.getEmail());
		if (user != null) {
			throw new AuthUserAlreadyException(authUser.getEmail(), "Already Exist");
		}
		authUser = service.create(authUser);
		log.info(authUser);
		return authUser;
	}

	/**
	 * 
	 * @param username
	 * @param authUser
	 * @return updated User
	 */
	@PutMapping(value = "/user/update/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AuthUser update(@PathVariable String username, @RequestBody AuthUser authUser) {
		log.info(username);
		authUser = service.update(authUser);
		log.info(authUser);
		return authUser;
	}

	/**
	 * 
	 * @param username
	 * @return deleted User email
	 */
	@DeleteMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String delete(@PathVariable String username) {
		log.info(username);
		username = service.delete(username);
		log.info(username);
		return username;
	}

	/**
	 * 
	 * @param principal
	 * @return logged in user eamil
	 */
	@GetMapping(value = "/user/loginEmail")
	public Map<String, String> getLoginEmail(Principal principal) {
		log.info("Map<String, String> getLoginEmail()");
		Map<String, String> map = new HashMap<String, String>();
		map.put("email", principal.getName());
		return map;
	}

	/**
	 * 
	 * @param username
	 * @return get user by email (checkEmailExist)
	 */

	@GetMapping(value = "/checkEmailExist/{email}")
	public AuthUser checkEmailExist(@PathVariable String email) {
		log.info(String.format("AuthUser getAuthUserByEamil(%s)", email));
		AuthUser authUser = service.getUserByEmail(email);
		log.info(authUser);

		if (authUser != null) {
			throw new AuthUserAlreadyException(email, "Already Exist");
		}
		return authUser;
	}

}
