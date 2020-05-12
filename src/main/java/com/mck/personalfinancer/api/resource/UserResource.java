package com.mck.personalfinancer.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mck.personalfinancer.api.dto.UserDTO;
import com.mck.personalfinancer.exception.AuthenticationError;
import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserResource {
	
	private UserService service;
	
	public UserResource(UserService service) {
		this.service = service;
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity authenticate(@RequestBody UserDTO dto) {
		try {		
			User authenticatedUser = service.authenticate(dto.getEmail(), dto.getPassword());			
			return ResponseEntity.ok(authenticatedUser);
		} catch (AuthenticationError e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity insert(@RequestBody UserDTO dto){
		User user = new User(null, dto.getName(), dto.getEmail(), dto.getPassword());
		try {
			User insertedUser = service.insertUser(user);			
			return new ResponseEntity<User>(insertedUser, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
