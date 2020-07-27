package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserTimesheet;

/**
 * 
 * @author
 *
 */
@Repository
public interface UserTimesheetRepository extends RootRepository<UserTimesheet, Integer> {

	@Query("select t from UserTimesheet t where t.authUser.email=:email")
	List<UserTimesheet> getUserTimesheetByEmail(@Param("email") String email);

//	@Query("select u from UserTimesheet u where u.authUser.email=:email order by u.loginDay desc ")
//	List<UserTimesheet> getLastLoginByDay(@Param("email") String email);

//	@Query("select max(u.loginDay) from UserTimesheet u where u.authUser.email=:email")
//	Date getMaxLoginDay(@Param("email") String email);

	@Query("select u from UserTimesheet u where u.logInTime <= :logInTime and u.logOutTime >= :logInTime and u.authUser.email=:email")
	List<UserTimesheet> findAllWithLogInTimeBetweenInOut(@Param("logInTime") Date logInTime,
			@Param("email") String email);

	@Query("select u from UserTimesheet u where u.logInTime <= :logOutTime and u.logOutTime >= :logOutTime and u.authUser.email=:email")
	List<UserTimesheet> findAllWithLogOutTimeBetweenInOut(@Param("logOutTime") Date logOutTime,
			@Param("email") String email);

	@Query("select u from UserTimesheet u where u.logInTime >= :logInTime and u.logInTime <= :logOutTime and u.authUser.email=:email")
	List<UserTimesheet> findAllLogInBetweenLogInTimeLogOut(@Param("logInTime") Date logInTime,
			@Param("logOutTime") Date logOutTime, @Param("email") String email);

	@Query("select u from UserTimesheet u where u.logOutTime >= :logInTime and u.logOutTime <= :logOutTime and u.authUser.email=:email")
	List<UserTimesheet> findAllLogOutBetweenLogInTimeLogOut(@Param("logInTime") Date logInTime,
			@Param("logOutTime") Date logOutTime, @Param("email") String email);
}
