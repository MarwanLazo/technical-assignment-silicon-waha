package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.log.AuthUserNotfoundException;
import com.example.demo.model.AuthUser;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AuthUserService service;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info(String.format("UserDetails loadUserByUsername( %s)", email));

		AuthUser user = service.getUserByEmail(email);
		log.info(user);

		if (user == null) {
			String error = String.format("User with Eamil :: %s not found", email);
			log.error(error);
			throw new AuthUserNotfoundException(email, error);
		}

		List<SimpleGrantedAuthority> authorities = Stream.of("ROLE_USER", "ROLE_ADMIN")
				.map(m -> new SimpleGrantedAuthority(m)).collect(Collectors.toList());

		return new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), authorities);
	}

}
