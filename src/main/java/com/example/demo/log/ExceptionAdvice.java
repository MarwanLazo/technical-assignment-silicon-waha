package com.example.demo.log;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		List<ConstraintError> errors = new ArrayList<ConstraintError>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {

			ConstraintError error = new ConstraintError(violation.getPropertyPath().toString(), violation.getMessage());
			errors.add(error);
			log.error(error);
		}
		log.error(errors);
		return new ResponseEntity<Object>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthUserNotfoundException.class)
	public ResponseEntity<Object> exception(AuthUserNotfoundException ex) {
		log.error(ex.getMessage());
		return new ResponseEntity<Object>(ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({ DateValidationException.class })
	public ResponseEntity<Object> handleConstraintViolation(DateValidationException ex) {

		List<ConstraintError> errors = new ArrayList<ConstraintError>();
		errors.add(new ConstraintError(ex.getProperty(), ex.getMessage()));
		log.error(errors);
		return new ResponseEntity<Object>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ AuthUserAlreadyException.class })
	public ResponseEntity<Object> handleConstraintViolation(AuthUserAlreadyException ex) {

		List<ConstraintError> errors = new ArrayList<ConstraintError>();
		errors.add(new ConstraintError(ex.getProperty(), ex.getErrorMessage()));
		log.error(errors);
		return new ResponseEntity<Object>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

}
