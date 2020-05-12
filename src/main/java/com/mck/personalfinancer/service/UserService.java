package com.mck.personalfinancer.service;

import com.mck.personalfinancer.model.entity.User;


public interface UserService {

	User authenticate(String email, String password);
	
	User insertUser(User user);
	
	void validateEmail(String email);
}
