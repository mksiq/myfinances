package com.mck.personalfinancer.service.impl;

import java.util.List;
import java.util.Objects;



import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.enums.TransactionStatus;
import com.mck.personalfinancer.repository.TransactionRepository;
import com.mck.personalfinancer.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

	private TransactionRepository repository;
	
	public TransactionServiceImpl(TransactionRepository repository) {
		this.repository = repository;
	}
	

	@Override
	@Transactional
	public Transaction insert(Transaction transaction) {
		// TODO validate
		return repository.save(transaction);
	}


	@Override
	@Transactional
	public Transaction update(Transaction transaction) {
		Objects.requireNonNull(transaction.getId());
		return repository.save(transaction);
	}

	@Override
	@Transactional
	public void delete(Transaction transaction) {
		Objects.requireNonNull(transaction.getId());
		repository.delete(transaction);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Transaction> findAll(Transaction filter) {
		Example<Transaction> example = Example.of( filter, ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	public void updateStatus(Transaction transaction, TransactionStatus status) {
		transaction.setStatus(status);
		update(transaction);
	}

}
