package com.example.demo.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DateValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String property;
	private final String message;

	

}
