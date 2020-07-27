package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
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

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

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

	@Test
	public void deleteAuthUser() throws Exception {

		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.delete("/user/".concat("user1@domain.net"))).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "user1@domain.net");
	}

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

	@Test
	public void getAllTimesheet() throws Exception {

		AuthUser user = userService.getUserByEmail("admin@domain.net");
		UserTimesheet t = new UserTimesheet(null, user, new Date(), new Date());
		timesheetService.create(t);

//		MvcResult mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
//				.perform(MockMvcRequestBuilders.get("/timesheet").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
//		int status = mvcResult.getResponse().getStatus();
//		assertEquals(200, status);
//		String content = mvcResult.getResponse().getContentAsString();
//		UserTimesheet[] users = mapFromJson(content, UserTimesheet[].class);
//		assertTrue(users.length > 0);

		MvcResult mvcResult = MockMvcBuilders
				.webAppContextSetup(webApplicationContext).build().perform(MockMvcRequestBuilders
						.get("/timesheet/".concat(user.getEmail())).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		UserTimesheet[] users = mapFromJson(content, UserTimesheet[].class);
		assertTrue(users.length > 0);

		UserTimesheet userTSH = new UserTimesheet(null, user, new Date(), new Date());
		String inputJson = mapToJson(userTSH);
		mvcResult = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
				.perform(MockMvcRequestBuilders.post("/timesheet/create/".concat(user.getEmail()))
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		content = mvcResult.getResponse().getContentAsString();
		UserTimesheet userTsheet = mapFromJson(content, UserTimesheet.class);
		userTSH.setId(userTsheet.getId());
		assertEquals(mapToJson(userTSH), mapToJson(userTsheet));

		timesheetService.delete(t);

	}

	@Test
	public void testUserDetailsService() {
		assertThat(userDetailsService.loadUserByUsername("admin@domain.net").getUsername())
				.isEqualTo("admin@domain.net");
	}

}
