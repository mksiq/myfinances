package com.mck.personalfinancer.service;

import java.util.Optional;

import com.mck.personalfinancer.model.entity.User;


public interface UserService {

	User authenticate(String email, String password);
	
	User insertUser(User user);
	
	void validateEmail(String email);
	
	Optional<User> findById(Long id);
}
