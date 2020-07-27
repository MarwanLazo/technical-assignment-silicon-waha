package com.example.demo.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.log.DateValidationException;
import com.example.demo.model.UserTimesheet;
import com.example.demo.service.AuthUserService;
import com.example.demo.service.UserTimesheetService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@AllArgsConstructor
public class UserTimesheetController {

	private final UserTimesheetService service;
	private final AuthUserService authUserService;

	/**
	 * 
	 * @param email
	 * @return All UserTimesheet for Current User by email
	 */
	@GetMapping(value = "/timesheet/{email}")
	public List<UserTimesheet> getAllByUsername(@PathVariable String email) {
		log.info(String.format("List<UserTimesheet>  getAllByUsername(%s)", email));
		List<UserTimesheet> tSheet = service.getUserTimesheetByEmail(email);
		tSheet.sort(Comparator.comparing(UserTimesheet::getLogInTime).reversed());
		log.info(tSheet);
		return tSheet;

	}

	/**
	 * 
	 * @param tSheet create UserTimesheet
	 * @return created UserTimesheet
	 * 
	 */

	@PostMapping(value = "/timesheet/create/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTimesheet create(@PathVariable String email, @RequestBody UserTimesheet tSheet) {
		log.info(String.format("UserTimesheet create(%s)", tSheet));

		tSheet.setAuthUser(authUserService.getUserByEmail(email));
		if (tSheet.getLogInTime() == null || tSheet.getLogOutTime() == null) {
			service.create(tSheet);
		}

		if (tSheet.getLogInTime().after(new Date()) || tSheet.getLogOutTime().after(new Date())) {
			throw new DateValidationException("logInTime/logOutTime", "both must be before or equal now ");
		}

		if (tSheet.getLogInTime().after(tSheet.getLogOutTime())) {
			throw new DateValidationException("logInTime/logOutTime", "logInTime After logOutTime");
		}

		if (!service.findAllWithLogInTimeBetweenInOut(tSheet.getLogInTime(), email).isEmpty()
				|| !service.findAllWithLogOutTimeBetweenInOut(tSheet.getLogOutTime(), email).isEmpty()
				|| !service.findAllLogInBetweenLogInTimeLogOut(tSheet.getLogInTime(), tSheet.getLogOutTime(), email)
						.isEmpty()
				|| !service.findAllLogOutBetweenLogInTimeLogOut(tSheet.getLogInTime(), tSheet.getLogOutTime(), email)
						.isEmpty()) {
			throw new DateValidationException("logInTime/logOutTime",
					"Log interval must not conflict with other intervals");
		}

		UserTimesheet userTimesheet = service.create(tSheet);
		log.info(String.format("%s created success ", userTimesheet));

		return userTimesheet;

	}
}
