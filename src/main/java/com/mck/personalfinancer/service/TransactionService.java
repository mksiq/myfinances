package com.mck.personalfinancer.service;

import java.util.List;

import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.enums.TransactionStatus;


public interface TransactionService {

	Transaction insert(Transaction t);
	
	Transaction update(Transaction t);
	
	void delete(Transaction t);
	
	List<Transaction> findAll( Transaction filter);
	
	void updateStatus(Transaction t, TransactionStatus s);
}
