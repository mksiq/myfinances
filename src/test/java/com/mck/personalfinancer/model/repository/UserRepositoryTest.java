package com.mck.personalfinancer.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.repository.UserRepository;


@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {
	
	@Autowired
	UserRepository repo;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void verifyIfEmailExists() {
		User user = createUser();	
		entityManager.persist(user);
		boolean result = repo.existsByEmail("maickel@gmail.com");
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void verifyAsFalseWhenThereIsNoEmail() {
		boolean result = repo.existsByEmail("siqueira@gmail.com");
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void shallInsertOneUserInDB() {
		User user = createUser();	
		User insertedUser = repo.save(user);
		Assertions.assertThat(insertedUser.getId()).isNotNull();
	}
	
	@Test
	public void shallSelectOneUserByEmail() {
		User user = createUser();
		entityManager.persist(user);
		
		Optional<User> selectedUser = repo.findByEmail("maickel@gmail.com");
		
		Assertions.assertThat(selectedUser.isPresent()).isTrue();
	}
	
	@Test
	public void shallReturnEmptyWhenSelectOneUserByEmail() {
		Optional<User> selectedUser = repo.findByEmail("emailnotregistered@gmail.com");
		
		Assertions.assertThat(selectedUser.isPresent()).isFalse();
	}
	
	public static User createUser() {
		User user = new User();
		user.setName("maick");
		user.setEmail("maickel@gmail.com");
		user.setPassword("12345");
		return user;
	}
}
