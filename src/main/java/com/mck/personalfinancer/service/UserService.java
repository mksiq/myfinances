package com.mck.personalfinancer.service;

import com.mck.personalfinancer.model.entity.User;


public interface UserService {

	User authenticate(String email, String password);
	
	User inserUser(User user);
	
	void validateEmail(String email);
}
