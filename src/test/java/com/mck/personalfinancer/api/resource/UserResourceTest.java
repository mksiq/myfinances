package com.mck.personalfinancer.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mck.personalfinancer.api.dto.UserDTO;
import com.mck.personalfinancer.exception.AuthenticationError;
import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.service.TransactionService;
import com.mck.personalfinancer.service.UserService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserResource.class)
@AutoConfigureMockMvc
public class UserResourceTest {

	static final String API = "/api/users/";
	static final MediaType JSON = MediaType.APPLICATION_JSON;

	@Autowired
	MockMvc mvc;
	@MockBean
	UserService service;
	@MockBean
	TransactionService transactionService;

	@Test
	public void shallAuthenticateOneUser() throws Exception {
		UserDTO dto = new UserDTO();
		String email = "email@email.ca";
		String password = "12345";
		dto.setEmail(email);
		dto.setPassword(password);
		User user = new User(1l, "tester", "email@email.ca", "12345");

		Mockito.when(service.authenticate(email, password)).thenReturn(user);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("authenticate")).accept(JSON)
				.contentType(JSON).content(json);
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()));
	}

	@Test
	public void shallDenyAuthenticationWrongPassword() throws Exception {
		UserDTO dto = new UserDTO();
		String email = "email@email.ca";
		String password = "12345";
		dto.setEmail(email);
		dto.setPassword(password);

		Mockito.when(service.authenticate(email, password)).thenThrow(AuthenticationError.class);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/authenticate")).accept(JSON)
				.contentType(JSON).content(json);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void shallInsertOneUser() throws Exception {

		String email = "email@email.ca";
		String password = "12345";
		User user = new User(2L, null, email, password);
		UserDTO dto = new UserDTO();
		dto.setEmail(email);
		dto.setPassword(password);

		Mockito.when(service.insertUser(Mockito.any(User.class))).thenReturn(user);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON).contentType(JSON)
				.content(json);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()));
		;
//		System.out.println("*****\n\n" + json + "\n\n*****");
	}
	
	@Test
	public void shallReturnBadRequestOnInvalidUser() throws Exception {

		String email = "email@email.ca";
		String password = "12345";
		UserDTO dto = new UserDTO();
		dto.setEmail(email);
		dto.setPassword(password);

		Mockito.when(service.insertUser(Mockito.any(User.class))).thenThrow(BusinessRuleException.class);
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON).contentType(JSON)
				.content(json);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
