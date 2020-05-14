package com.mck.personalfinancer.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mck.personalfinancer.api.dto.UserDTO;
import com.mck.personalfinancer.exception.AuthenticationError;
import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.service.TransactionService;
import com.mck.personalfinancer.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserResource {
	
	private UserService service;
	private TransactionService transactionService;
	
	public UserResource(UserService service, TransactionService transactionService) {
		this.service = service;
		this.transactionService = transactionService;
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
	
	@GetMapping("{id}/balance")
	public ResponseEntity getBalance(@PathVariable("id") Long id) {
		Optional<User> user = service.findById(id);
		if(!user.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		BigDecimal balance = transactionService.getBalanceByUser(id);
		return ResponseEntity.ok(balance);
	}
}
