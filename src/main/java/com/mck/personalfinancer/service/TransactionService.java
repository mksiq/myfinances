package com.mck.personalfinancer.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.enums.TransactionStatus;


public interface TransactionService {

	Transaction insert(Transaction t);
	
	Transaction update(Transaction t);
	
	void delete(Transaction t);
	
	List<Transaction> findAll( Transaction filter);
	
	void updateStatus(Transaction t, TransactionStatus s);
	
	void validate(Transaction t);
	
	Optional<Transaction> findById(Long id);
	
	BigDecimal getBalanceByUser(Long idUser);
}
