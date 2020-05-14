package com.mck.personalfinancer.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.enums.TransactionStatus;
import com.mck.personalfinancer.model.enums.TransactionType;
import com.mck.personalfinancer.repository.TransactionRepository;
import com.mck.personalfinancer.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

	private TransactionRepository repository;
	
	public TransactionServiceImpl(TransactionRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Optional<Transaction> findById(Long id){
		return repository.findById(id);
	}
	
	@Override
	@Transactional
	public Transaction insert(Transaction transaction) {
	//	validate(transaction);
		transaction.setStatus(TransactionStatus.PENDING);
		return repository.save(transaction);
	}

	@Override
	@Transactional
	public Transaction update(Transaction transaction) {
		Objects.requireNonNull(transaction.getId());
		validate(transaction);
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

	@Override
	public void validate(Transaction transaction) {	
		if(transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
			throw new BusinessRuleException("You need a Description.");
		}
		if(transaction.getMonth() == null || transaction.getMonth() < 1 || transaction.getMonth() > 12) {
			throw new BusinessRuleException("Give a valid month.");
		}
		if(transaction.getYear() == null || transaction.getYear() < 1000) {
			throw new BusinessRuleException("Give a valid year.");
		}
		if(transaction.getUser() == null || transaction.getUser().getId() == null) {
			throw new BusinessRuleException("Inform an User.");
		}
		if(transaction.getValue() == null || transaction.getValue().compareTo(BigDecimal.ZERO) < 1) {
			throw new BusinessRuleException("Inform a positive value.");
		}
		if(transaction.getType() == null) {
			throw new BusinessRuleException("Give the type of the transaction.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getBalanceByUser(Long id) {
		BigDecimal incomes = repository.getBalanceByTypeOfTransactionAndUser(id, TransactionType.INCOME);
		BigDecimal expenses = repository.getBalanceByTypeOfTransactionAndUser(id, TransactionType.EXPENSE);
		
		if(incomes == null) {
			incomes = BigDecimal.ZERO;
		}
		if(expenses == null) {
			expenses = BigDecimal.ZERO;
		}
		
		return incomes.subtract(expenses);
	}
}
