package com.mck.personalfinancer.service;



import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mck.personalfinancer.exception.AuthenticationError;
import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.repository.UserRepository;
import com.mck.personalfinancer.service.impl.UserServiceImpl;

@SpringBootTest
@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	@SpyBean
	UserServiceImpl service;
	@MockBean
	UserRepository repo;
	
	@Test
	public void shallSaveOneUser() {
		Mockito.doNothing().when(service).validateEmail(Mockito.anyString());
		User user = new User(); user.setName("Marcus Aurelius"); user.setEmail("email@gmail.com"); user.setPassword("paspasswordsword"); user.setId(1l);
		
		Mockito.when(repo.save(Mockito.any(User.class))).thenReturn(user);
		
		User insertedUser = service.insertUser(new User());
		
		org.assertj.core.api.Assertions.assertThat(insertedUser).isNotNull();
		org.assertj.core.api.Assertions.assertThat(insertedUser.getId()).isEqualTo(1L);
		org.assertj.core.api.Assertions.assertThat(insertedUser.getName()).isEqualTo("Marcus Aurelius");
		org.assertj.core.api.Assertions.assertThat(insertedUser.getEmail()).isEqualTo("email@gmail.com");
		org.assertj.core.api.Assertions.assertThat(insertedUser.getPassword()).isEqualTo("paspasswordsword");	
	}
	@Test
	public void shouldNotInsertUserWithAlreadyRegisteredEmail() {
		String email = "email@email.com";
		User user = new User();user.setEmail(email);
		Mockito.doThrow(BusinessRuleException.class).when(service).validateEmail(email);
		try {
			service.insertUser(user);
		} catch (BusinessRuleException e) {
			// Do nothing
		}	
		Mockito.verify(repo, Mockito.never()).save(user);
	}
	
	
	@Test
	public void shallAuthenticateOneUserSuccessfully() {
		String email = "email@gmail.com";
		String password = "password";
		
		User user = new User(); user.setEmail(email); user.setPassword(password); user.setId(25l);
		
		Mockito.when(repo.findByEmail(email)).thenReturn(Optional.of(user));
		
		User result = service.authenticate(email, password);
		
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void validadeEmail() {
		Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		service.validateEmail("maick@gmail.com");
	}
	
	
	@Test
	public void shallThrowErrorIfDoesntFindEmail() {
		Assertions.assertThrows(AuthenticationError.class, () -> {
			Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			service.authenticate("maick@email.com", "123");
		});
	}
	@Test
	public void shallThrowErrorIfWrongPassword() {
		User user = new User(); user.setEmail("maick@gmail.com"); user.setPassword("12345"); user.setId(1l);
		Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable( () -> service.authenticate("maick@gmail.com" , "123"));
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(AuthenticationError.class).hasMessage("Wrong password");		
//		Assertions.assertThrows(AuthenticationError.class, () -> {
//			Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
//			service.authenticate("maick@email.com", "123");
//		});
	}
	
	@Test
	public void throwsErrorIfEmailIsRegistered() {
		Mockito.when(repo.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		Assertions.assertThrows(BusinessRuleException.class, () -> {
			service.validateEmail("maick@gmail.com");
		});
	}
}
