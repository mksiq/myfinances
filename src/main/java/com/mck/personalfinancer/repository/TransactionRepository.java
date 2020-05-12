package com.mck.personalfinancer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mck.personalfinancer.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
