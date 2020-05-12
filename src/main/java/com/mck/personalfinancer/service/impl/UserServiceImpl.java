package com.mck.personalfinancer.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mck.personalfinancer.exception.AuthenticationError;
import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.repository.UserRepository;
import com.mck.personalfinancer.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	
	private UserRepository repo;
	
	@Autowired
	public UserServiceImpl(UserRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public User authenticate(String email, String password) {
		Optional<User> user = repo.findByEmail(email);
		if(!user.isPresent()) {
			throw new AuthenticationError("User not found with email "+email);
		}
		if(!user.get().getPassword().equals(password)) {
			throw new AuthenticationError("Wrong password");
		}
		
		return user.get();
	}

	@Override
	@Transactional
	public User insertUser(User user) {
		validateEmail(user.getEmail());
		return repo.save(user);
	}

	@Override
	public void validateEmail(String email) {
		boolean exists = repo.existsByEmail(email);
		if(exists) {
			throw new BusinessRuleException("This email is already registered.");
		}
	}
}
