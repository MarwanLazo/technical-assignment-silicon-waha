package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.AuthUser;
import com.example.demo.repository.AuthUserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AuthUserServiceImpl extends DAOServiceImpl<AuthUser, String> implements AuthUserService {

	private final AuthUserRepository repository;

	public AuthUserServiceImpl(AuthUserRepository repository) {
		super(repository);
		this.repository = repository;

	}

	@Override
	public AuthUser getUserByEmail(String email) {
		log.info(String.format("AuthUser getAuthUserByEmail(String email) :: %s", email));
		AuthUser authUser = repository.getUserByEmail(email);
		log.info(String.format("Loaded User By Username :: %s", authUser));
		return authUser;
	}

	@Override
	public AuthUser getUserByUsername(String username) {
		log.info(String.format("AuthUser getUserByUsername(username) :: %s", username));
		AuthUser authUser = repository.getUserByUsername(username);
		log.info(authUser);
		return authUser;
	}

	
}
