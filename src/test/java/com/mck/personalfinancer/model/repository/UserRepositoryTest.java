package com.mck.personalfinancer.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.repository.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
	@Autowired
	UserRepository repo;
	
	@Test
	public void verifyIfEmailExists() {
		User user = new User();
		user.setName("maick");
		user.setEmail("maick@gmail.com");
		
		repo.save(user);
		
		
		boolean result = repo.existsByEmail("maick@gmail.com");
		
		Assertions.assertThat(result).isTrue();
	}
}
