package com.example.demo.service;

import java.util.Date;
import java.util.List;

import com.example.demo.model.UserTimesheet;

public interface UserTimesheetService extends DAOService<UserTimesheet, Integer> {

	List<UserTimesheet> getUserTimesheetByEmail(String email);

	List<UserTimesheet> findAllWithLogInTimeBetweenInOut(Date logInTime, String email);

	List<UserTimesheet> findAllWithLogOutTimeBetweenInOut(Date logOutTime, String email);

	List<UserTimesheet> findAllLogInBetweenLogInTimeLogOut(Date logInTime, Date logOutTime, String email);

	List<UserTimesheet> findAllLogOutBetweenLogInTimeLogOut(Date logInTime, Date logOutTime, String email);

}
