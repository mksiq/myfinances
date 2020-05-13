package com.mck.personalfinancer.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mck.personalfinancer.api.dto.TransactionDTO;
import com.mck.personalfinancer.api.dto.UpdateStatusDTO;
import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.model.enums.TransactionStatus;
import com.mck.personalfinancer.model.enums.TransactionType;
import com.mck.personalfinancer.service.TransactionService;
import com.mck.personalfinancer.service.UserService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionResource {

	private TransactionService service;
	private UserService userService;

	public TransactionResource(TransactionService service, UserService userService) {
		this.service = service;
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity find(@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "user") Long userId) {
		Transaction filter = new Transaction();
		filter.setDescription(description);
		filter.setMonth(month);
		filter.setYear(year);
		Optional<User> user = userService.findById(userId);
		if (!user.isPresent()) {
			return ResponseEntity.badRequest().body("User not found");
		} else {
			filter.setUser(user.get());
		}
		List<Transaction> list = service.findAll(filter);
		return ResponseEntity.ok(list);
	}

	@PostMapping
	public ResponseEntity insert(@RequestBody TransactionDTO objDto) {
		try {
			Transaction obj = fromDTO(objDto);
			service.insert(obj);
			return new ResponseEntity(obj, HttpStatus.CREATED);
		} catch (BusinessRuleException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity update(@PathVariable("id") Long id, @RequestBody TransactionDTO objDto) {
		objDto.setId(id);
		return service.findById(id).map(obj -> {
			try {
				Transaction newObj = fromDTO(objDto);
				service.update(newObj);
				return ResponseEntity.ok(newObj);
			} catch (BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Transaction not found.", HttpStatus.BAD_REQUEST));
	}

	@PutMapping("{id}/update-status")
	public ResponseEntity updateStatus(@PathVariable("id") Long id, @RequestBody UpdateStatusDTO objDto) {
		return service.findById(id).map(obj -> {
				TransactionStatus selected = TransactionStatus.valueOf(objDto.getStatus());
				if(selected == null) {
					return ResponseEntity.badRequest().body("Couldn't update transaction status. Send a valid status.");
				}		
				try {
					obj.setStatus(selected);
					service.update(obj);
					return ResponseEntity.ok(obj);
				} catch (BusinessRuleException e) {
					return ResponseEntity.badRequest().body(e.getMessage());
				}
		}).orElseGet(() -> new ResponseEntity("Transaction not found.", HttpStatus.BAD_REQUEST));			
	}

	@DeleteMapping("{id}")
	public ResponseEntity delete(@PathVariable("id") Long id) {
		return service.findById(id).map(obj -> {
			service.delete(obj);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Transaction not found.", HttpStatus.BAD_REQUEST));
	}

	private Transaction fromDTO(TransactionDTO objDto) {
		Transaction obj = new Transaction(objDto.getId(), objDto.getDescription(), objDto.getMonth(), objDto.getYear(),
				userService.findById(objDto.getUserId()).orElseThrow(() -> new BusinessRuleException("User not found")),
				objDto.getValue());
		if (objDto.getStatus() != null) {
			obj.setStatus(TransactionStatus.valueOf(objDto.getStatus()));
		}
		if (objDto.getType() != null) {
			obj.setType(TransactionType.valueOf(objDto.getType()));
		}
		return obj;
	}
}
