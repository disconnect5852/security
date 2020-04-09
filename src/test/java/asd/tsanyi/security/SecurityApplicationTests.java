package asd.tsanyi.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import asd.tsanyi.security.db.entity.User;
import asd.tsanyi.security.dto.InfoResponse;
import asd.tsanyi.security.enums.Roles;

@SpringBootTest(classes = SecurityApplication.class)
@WebAppConfiguration
@TestMethodOrder(OrderAnnotation.class)
class SecurityApplicationTests {
	protected static final String AUTH = "/authentication";
	private Logger log = LoggerFactory.getLogger(this.getClass());
	protected static final String TOKEN = "authorization";

	private static String testToken = "";
	private static long userId;

	@Autowired
	WebApplicationContext webApplicationContext;

	protected MockMvc mvc;

	@BeforeEach
	protected void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).alwaysDo(MockMvcResultHandlers.print()).build();
	}



	@Order(1)
	@Test
	void loginCheater() throws Exception {
		Map<String, String> content = new HashMap<String, String>();
		content.put("username", "cheater");
		content.put("password", "IDDQD");
		String json = mapToJson(content);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(AUTH).contentType(MediaType.APPLICATION_JSON_VALUE).content(json).characterEncoding("utf-8")).andReturn();
		int status = mvcResult.getResponse().getStatus();
		String response = mvcResult.getResponse().getContentAsString();
		InfoResponse infores = mapFromJson(response, InfoResponse.class);
		testToken = infores.getMessage();
		log.info("---------------STATUS: " + status + " Token: " + testToken);
		assertEquals(202, status);
	}
	@Order(2)
	@Test
	void createUser() throws Exception {
		Map<String, Object> content = new HashMap<String, Object>();
		content.put("username", RandomStringUtils.randomAlphabetic(6, 12));
		content.put("email", generateRandomEmailAddress());
		content.put("address", RandomStringUtils.randomAlphanumeric(10, 35));
		content.put("name", RandomStringUtils.randomAlphanumeric(10, 35));
		content.put("roles", Stream.of(Roles.USER).collect(Collectors.toCollection(HashSet::new)));
		String json = mapToJson(content);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/user").header(HttpHeaders.AUTHORIZATION, "Bearer "+testToken).contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		InfoResponse infores = mapFromJson(mvcResult.getResponse().getContentAsString(), InfoResponse.class);
		log.info("---------------STATUS: " + status + " Message: " + infores.getMessage());
		userId=getIntFromEndOfString(infores.getMessage());
		assertEquals(201, status);
	}
	
	@Order(3)
	@Test
	void getUserById() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/user/"+userId).header(HttpHeaders.AUTHORIZATION, "Bearer "+testToken)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertFalse(status==404);
		User user = mapFromJson(mvcResult.getResponse().getContentAsString(), User.class);
		log.info("---------------STATUS: " + status + " Username: " + user.getUsername());
		assertEquals(302, status);
	}
	
	@Order(4)
	@Test
	void deleteUserById() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete("/user/"+userId).header(HttpHeaders.AUTHORIZATION, "Bearer "+testToken)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertFalse(status==404 || status==410);
		InfoResponse response = mapFromJson(mvcResult.getResponse().getContentAsString(), InfoResponse.class);
		log.info("---------------STATUS: " + status + " Message: " + response.getMessage());
		assertEquals(200, status);
	}

	private static String generateRandomEmailAddress() {
		String randomMail = RandomStringUtils.randomAlphabetic(4, 16) + "@" + RandomStringUtils.randomAlphabetic(4, 8) + "." + RandomStringUtils.randomAlphabetic(2);
		return randomMail;
	}
	
	private static int getIntFromEndOfString(String text) {
		final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
		Matcher matcher = lastIntPattern.matcher(text);
		int lastNumberInt=0;
		if (matcher.find()) {
		    lastNumberInt = Integer.parseInt(matcher.group(1));
		}
		return lastNumberInt;
	}
	
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> klassz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, klassz);
	}
}
