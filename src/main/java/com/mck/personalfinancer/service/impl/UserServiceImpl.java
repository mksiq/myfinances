package com.mck.personalfinancer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User inserUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEmail(String email) {
		boolean exists = repo.existsByEmail(email);
		if(exists) {
			throw new BusinessRuleException("This email is already registered.");
		}
	}
}
