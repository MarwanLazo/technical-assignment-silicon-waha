package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.demo.model.AuthUser;
import com.example.demo.service.AuthUserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Marwan
 * 
 *
 */
@Log4j2
@SpringBootApplication
public class TechnicalAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechnicalAssignmentApplication.class, args);
	}

	@AllArgsConstructor
	@Component
	public class Intialization {

		private final AuthUserService service;

		/**
		 * App Setup for test
		 */
		@EventListener(classes = ApplicationReadyEvent.class)
		public void intialize() {
			log.info("void intialize()  Start");
			AuthUser user = service.create(new AuthUser("admin@domain.net", "admin", "admin", "012543625478"));
			log.info(String.format("load user %s Success", user));
		}
	}
}
