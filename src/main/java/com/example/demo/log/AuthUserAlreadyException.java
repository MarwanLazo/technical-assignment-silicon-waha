package com.example.demo.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthUserAlreadyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String property;
	private final String errorMessage;

}
