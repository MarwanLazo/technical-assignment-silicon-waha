package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.UserTimesheet;
import com.example.demo.repository.UserTimesheetRepository;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Marwan
 *
 */
@Log4j2
@Service
public class UserTimesheetServiceImpl extends DAOServiceImpl<UserTimesheet, Integer> implements UserTimesheetService {

	private final UserTimesheetRepository repository;

	public UserTimesheetServiceImpl(UserTimesheetRepository repository) {
		super(repository);
		this.repository = repository;
	}

	/**
	 * @param email
	 * @return list UserTimesheet by email
	 */

	@Override
	public List<UserTimesheet> getUserTimesheetByEmail(String email) {
		log.info(String.format("getUserTimesheetByUsername(%s)", email));
		List<UserTimesheet> tSheet = repository.getUserTimesheetByEmail(email);
		log.info(tSheet);
		return tSheet;
	}

	/**
	 * @param logInTime
	 * @return list all time sheets logInTime between saved login / logout Date
	 */
	@Override
	public List<UserTimesheet> findAllWithLogInTimeBetweenInOut(Date logInTime, String email) {
		log.info(String.format("List<UserTimesheet> findAllWithLogInTimeBetweenInOut(%s)", logInTime));
		List<UserTimesheet> tSheetList = repository.findAllWithLogInTimeBetweenInOut(logInTime, email);
		log.info(tSheetList);
		return tSheetList;
	}

	/**
	 * @param logOutTime
	 * @return list all time sheets logOutTime between saved login / logout Date
	 */
	@Override
	public List<UserTimesheet> findAllWithLogOutTimeBetweenInOut(Date logOutTime, String email) {
		log.info(String.format("List<UserTimesheet> findAllWithLogOutTimeBetweenInOut(%s)", logOutTime));
		List<UserTimesheet> tSheetList = repository.findAllWithLogOutTimeBetweenInOut(logOutTime, email);
		log.info(tSheetList);
		return tSheetList;
	}

	/**
	 * @param logInTime
	 * @param logOutTime
	 * 
	 * @return all LogIn TimeSheet between new logInTime and logOutTime
	 */

	@Override
	public List<UserTimesheet> findAllLogInBetweenLogInTimeLogOut(Date logInTime, Date logOutTime, String email) {
		log.info(
				String.format("List<UserTimesheet> findAllLogInBetweenLogInTimeLogOut(%s, %s)", logInTime, logOutTime));
		List<UserTimesheet> tSheetList = repository.findAllLogInBetweenLogInTimeLogOut(logInTime, logOutTime, email);
		log.info(tSheetList);
		return tSheetList;
	}

	/**
	 * @param logInTime
	 * @param logOutTime
	 * 
	 * @return all Logout TimeSheet between new logInTime and logOutTime
	 */

	@Override
	public List<UserTimesheet> findAllLogOutBetweenLogInTimeLogOut(Date logInTime, Date logOutTime, String email) {
		log.info(String.format("List<UserTimesheet> findAllLogOutBetweenLogInTimeLogOut(%s, %s)", logInTime,
				logOutTime));
		List<UserTimesheet> tSheetList = repository.findAllLogOutBetweenLogInTimeLogOut(logInTime, logOutTime, email);
		log.info(tSheetList);
		return tSheetList;
	}
//	@Override
//	public Date getMaxLoginDay(String email) {
//		log.info(String.format("Date getMaxLoginDay(%s)", email));
//		Date maxDate = repository.getMaxLoginDay(email);
//		log.info(maxDate);
//		return maxDate;
//	}
}
