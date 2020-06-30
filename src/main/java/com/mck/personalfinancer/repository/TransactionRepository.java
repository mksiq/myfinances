package com.mck.personalfinancer.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.enums.TransactionStatus;
import com.mck.personalfinancer.model.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	
	@Query(value = "SELECT SUM(t.value) FROM Transaction t JOIN t.user u where u.id = :idUser AND t.type = :type AND t.status = :status GROUP BY u ")
	BigDecimal getBalanceByTypeOfTransactionAndUser(@Param("idUser") Long idUser, @Param("type") TransactionType type, @Param("status") TransactionStatus status);

}
