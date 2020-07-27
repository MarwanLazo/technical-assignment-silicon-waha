package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.controller.AuthUserController;
import com.example.demo.log.ConstraintError;
import com.example.demo.model.AuthUser;
import com.example.demo.model.UserTimesheet;
import com.example.demo.service.AuthUserService;
import com.example.demo.service.UserTimesheetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = TechnicalAssignmentApplication.class)
@WebAppConfiguration
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
class TechnicalAssignmentApplicationTests {

	private @Autowired WebApplicationContext webApplicationContext;
	private @Autowired EntityManager em;
	private @Autowired AuthUserService userService;
	private @Autowired AuthUserController login;
	private @Autowired UserTimesheetService timesheetService;

	private @Autowired UserDetailsService userDetailsService;

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 * 
	 *                                 Map DTO To Json Object
	 * 
	 */
	private String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * 
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 
	 *                              map json object To DTO
	 */
	private <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	/**
	 * 
	 * @throws Exception
	 * 
	 *                   Test API load all users
	 */
	@Test
	public void getAllAuthUser() throws Exception {

		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.get("/user/all").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		AuthUser[] users = mapFromJson(content, AuthUser[].class);
		assertTrue(users.length > 0);
	}

	/**
	 * 
	 * @throws Exception
	 * 
	 *                   Test API Create User
	 */

	@Test
	public void createAuthUser() throws Exception {
		AuthUser authUser = new AuthUser("user177@domain.net", "user1", "user1", "0100152478");

		String inputJson = mapToJson(authUser);
		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.post("/user/create").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, mapToJson(authUser));
		userService.delete(authUser);
	}

	/**
	 * 
	 * @throws Exception
	 * 
	 *                   Test Check API Validation Create User
	 */
	@Test
	public void checkCreateAuthUserValidation() throws Exception {
		AuthUser authUser = new AuthUser("user251@domain.net", "user1", "user122555555", "0100152478");

		String inputJson = mapToJson(authUser);
		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.post("/user/create").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "[{\"property\":\"password\",\"error\":\"size must be between 3 and 8\"}]");
	}

	/**
	 * 
	 * @throws Exception Test Update User API
	 */
	@Test
	public void updateAuthUser() throws Exception {
		AuthUser authUser = new AuthUser("user1@domain.net", "user1", "user123", "0111110000");

		String inputJson = mapToJson(authUser);
		MvcResult mvcResult = MockMvcBuilders
				.webAppContextSetup(webApplicationContext).build().perform(MockMvcRequestBuilders
						.put("/user/update/user1").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, mapToJson(authUser));
	}

	/**
	 * Test delete User API
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteAuthUser() throws Exception {

		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.delete("/user/".concat("user1@domain.net"))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "user1@domain.net");
	}

	/**
	 * Test User Service CRUD
	 */
	@Test
	void contextLoadUser() {
		AuthUser t = new AuthUser("user2@domain.net", "user2", "user2", "0100152478");
		userService.create(t);
		assertThat(t).isEqualTo(userService.create(t));

		assertThat(t).isEqualTo(userService.getUserByUsername("user2"));

		assertThat(login.getAll()).isEqualTo(em.createQuery("select t from AuthUser t ").getResultList());

		t.setMobile("01000634791");
		assertThat(t).isEqualTo(userService.update(t));

		assertThat(t).isEqualTo(userService.delete(t));
		userService.delete(t);
	}

	/**
	 * Test User Time Sheet service CRUD
	 */

	@Test
	void contextLoadTimesheet() {

		AuthUser user = userService.getUserByEmail("admin@domain.net");
		UserTimesheet t = new UserTimesheet(null, user, new Date(), new Date());

		assertThat(user).isEqualTo(timesheetService.create(t).getAuthUser());

//		t.setLogType(LogType.log_out);
		assertThat(t).isEqualTo(timesheetService.update(t));

		assertThat(t).isEqualTo(timesheetService.delete(t));

		assertThat(timesheetService.getAll())
				.isEqualTo(em.createQuery("select t from UserTimesheet t ").getResultList());

	}

	/**
	 * Load all time sheet by User
	 * 
	 * @throws Exception
	 */

	@Test
	public void getAllTimesheet() throws Exception {

		AuthUser user = userService.getUserByEmail("admin@domain.net");
		UserTimesheet t = new UserTimesheet(null, user, new Date(), new Date());
		timesheetService.create(t);

		MvcResult mvcResult = MockMvcBuilders
				.webAppContextSetup(webApplicationContext).build().perform(MockMvcRequestBuilders
						.get("/timesheet/".concat(user.getEmail())).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		UserTimesheet[] users = mapFromJson(content, UserTimesheet[].class);
		assertTrue(users.length > 0);
	}

	/**
	 * Test API to Create Time Sheet
	 * 
	 * @throws Exception
	 */
	@Test
	public void createTimesheet() throws Exception {

		AuthUser user = userService.getUserByEmail("admin@domain.net");
		UserTimesheet userTSH = new UserTimesheet(null, user, new Date(), new Date());
		String inputJson = mapToJson(userTSH);
		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.post("/timesheet/create/".concat(user.getEmail()))
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		UserTimesheet userTsheet = mapFromJson(content, UserTimesheet.class);
		userTSH.setId(userTsheet.getId());
		assertEquals(mapToJson(userTSH), mapToJson(userTsheet));

		timesheetService.delete(userTSH);

	}

	/**
	 * Test Insert User time sheet validation (null, exceed Current Time ,log time
	 * Interval conflict)
	 * 
	 * @throws Exception
	 */
	@Test
	public void createTimesheetWithConflectInterval() throws Exception {

		AuthUser user = userService.getUserByEmail("admin@domain.net");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 3);
		Date loginTime = c.getTime();

		c.clear();
		c.setTime(new Date());
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 3);
		Date logOutTime = c.getTime();

		UserTimesheet userTSH = new UserTimesheet(null, user, loginTime, logOutTime);
		UserTimesheet saved = timesheetService.create(userTSH);

		// booth login/logout null
		userTSH = new UserTimesheet(null, user, null, null);
		String inputJson = mapToJson(userTSH);
		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.post("/timesheet/create/".concat(user.getEmail()))
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		String content = mvcResult.getResponse().getContentAsString();
		ConstraintError[] errors = mapFromJson(content, ConstraintError[].class);
		assertTrue(errors.length > 0);

		// booth login/logout Exceed Now can't log out after some time
		c.clear();
		c.setTime(new Date());
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 5);
		loginTime = c.getTime();

		c.clear();
		c.setTime(new Date());
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 5);
		logOutTime = c.getTime();
		userTSH = new UserTimesheet(null, user, loginTime, logOutTime);

		inputJson = mapToJson(userTSH);
		mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.post("/timesheet/create/".concat(user.getEmail()))
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		content = mvcResult.getResponse().getContentAsString();
		errors = mapFromJson(content, ConstraintError[].class);
		assertTrue(errors.length > 0);
		// Interval logging conflict
		c.clear();
		c.setTime(new Date());
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) - 1);
		loginTime = c.getTime();

		c.clear();
		c.setTime(new Date());
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 5);
		logOutTime = c.getTime();

		userTSH = new UserTimesheet(null, user, loginTime, logOutTime);

		inputJson = mapToJson(userTSH);
		mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.post("/timesheet/create/".concat(user.getEmail()))
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		content = mvcResult.getResponse().getContentAsString();
		errors = mapFromJson(content, ConstraintError[].class);
		assertTrue(errors.length > 0);

		timesheetService.delete(userTSH);
		timesheetService.delete(saved);

	}

	/**
	 * UserDetailsService Test GEt user
	 */

	@Test
	public void testUserDetailsService() {
		assertThat(userDetailsService.loadUserByUsername("admin@domain.net").getUsername())
				.isEqualTo("admin@domain.net");
	}

	/**
	 * Test API checkEmailExist
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkEmailExist() throws Exception {

		String email = "notsavedOne@domain.net";
		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build().perform(
				MockMvcRequestBuilders.get("/checkEmailExist/".concat(email)).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "");

		email = "admin@domain.net";
		mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build().perform(
				MockMvcRequestBuilders.get("/checkEmailExist/".concat(email)).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
		content = mvcResult.getResponse().getContentAsString();
		ConstraintError[] errors = mapFromJson(content, ConstraintError[].class);
		assertTrue(errors.length > 0);

	}

}
