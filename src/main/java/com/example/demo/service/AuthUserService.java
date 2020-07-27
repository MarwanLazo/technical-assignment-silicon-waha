package com.example.demo.service;

import com.example.demo.model.AuthUser;

public interface AuthUserService extends DAOService<AuthUser, String> {


	AuthUser getUserByUsername(String username);

	AuthUser getUserByEmail(String email);

}
