package com.mck.personalfinancer.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mck.personalfinancer.exception.BusinessRuleException;
import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.entity.User;
import com.mck.personalfinancer.model.enums.TransactionStatus;
import com.mck.personalfinancer.model.enums.TransactionType;
import com.mck.personalfinancer.model.repository.TransactionRepositoryTest;
import com.mck.personalfinancer.repository.TransactionRepository;
import com.mck.personalfinancer.service.impl.TransactionServiceImpl;

@SpringBootTest
@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
public class TransactionServiceTest {

	@SpyBean
	TransactionServiceImpl service;
	@MockBean
	TransactionRepository repository;
	
	@Test
	public void shallInsertOneTransaction() {
		Transaction transactionToInsert = TransactionRepositoryTest.createTransaction();
		Mockito.doNothing().when(service).validate(transactionToInsert);
		Transaction insertedTransaction = TransactionRepositoryTest.createTransaction();
		insertedTransaction.setId(1l);
		Mockito.when(repository.save(transactionToInsert)).thenReturn(insertedTransaction);
		
		Transaction transaction = service.insert(transactionToInsert);
		
		Assertions.assertThat(transaction.getId()).isEqualTo(insertedTransaction.getId());
		Assertions.assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
	}
	
//	@Test
//	public void shallNotSaveOneTransactionIfValidationError() {
//		Transaction transactionToInsert = TransactionRepositoryTest.createTransaction();
//		Mockito.doThrow(BusinessRuleException.class).when(service).validate(transactionToInsert);
//		
//		Assertions.catchThrowableOfType( () -> service.insert(transactionToInsert), BusinessRuleException.class);
//		
//		Mockito.verify(repository, Mockito.never()).save(transactionToInsert);
//	}
	
	@Test
	public void shallUpdateOneTransaction() {
		Transaction insertedTransaction = TransactionRepositoryTest.createTransaction();
		insertedTransaction.setId(1l);
		Mockito.doNothing().when(service).validate(insertedTransaction);
				
		Mockito.when(repository.save(insertedTransaction)).thenReturn(insertedTransaction);
		
		service.update(insertedTransaction);
		
		Mockito.verify(repository, Mockito.times(1)).save(insertedTransaction);
	}
	
	@Test
	public void shallGiveErrorOnUpdateOneTransactionNotExisting() {
		Transaction transactionToInsert = TransactionRepositoryTest.createTransaction();
		
		Assertions.catchThrowableOfType( () -> service.update(transactionToInsert), NullPointerException.class);
		
		Mockito.verify(repository, Mockito.never()).save(transactionToInsert);
	}
	
	@Test
	public void shallDeleteOneTransaction() {
		Transaction transaction = TransactionRepositoryTest.createTransaction();
		transaction.setId(1l);
		
		service.delete(transaction);
		
		Mockito.verify(repository).delete(transaction);
	}
	
	@Test
	public void shallNotDeleteOneTransactionNotYetInserted() {
		Transaction transaction = TransactionRepositoryTest.createTransaction();
		
		Assertions.catchThrowableOfType( () -> service.delete(transaction), NullPointerException.class);
		
		Mockito.verify(repository, Mockito.never() ).delete(transaction);
	}
	
	@Test
	public void shallFilterTransaction() {
		Transaction transaction = TransactionRepositoryTest.createTransaction();
		transaction.setId(1l);
		
		List<Transaction> list = Arrays.asList(transaction);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(list);
		
		List<Transaction> foundList = service.findAll(transaction);
		
		Assertions.assertThat(foundList).isNotEmpty().hasSize(1).contains(transaction);
	}
	
	@Test
	public void shallUpdateOneTransactionStatus() {
		Transaction transaction = TransactionRepositoryTest.createTransaction();
		transaction.setId(1l);
		transaction.setStatus(TransactionStatus.PENDING);
		
		TransactionStatus status = TransactionStatus.CANCELED;
		Mockito.doReturn(transaction).when(service).update(transaction);
		service.updateStatus(transaction, status);
		
		Assertions.assertThat(transaction.getStatus()).isEqualTo(status);
		Mockito.verify(service).update(transaction);
	}
	
	@Test
	public void shallGetOneTransactionById() {
		Long id = 1l;
		Transaction transaction = TransactionRepositoryTest.createTransaction();
		transaction.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(transaction));
		Optional<Transaction> foundTransaction = service.findById(id);
		
		Assertions.assertThat(foundTransaction.isPresent()).isTrue();
	}
	
	@Test
	public void shallReturnEmptyIfGetNonExistentTransactionById() {
		Long id = 1l;
		Transaction transaction = TransactionRepositoryTest.createTransaction();
		transaction.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		Optional<Transaction> foundTransaction = service.findById(id);
		
		Assertions.assertThat(foundTransaction.isPresent()).isFalse();
	}
	
	@Test
	public void shallThrowErrorsWhileValidatingTransaction() {
		Transaction transaction = new Transaction();

		Throwable error = Assertions.catchThrowable( () -> service.validate(transaction) );
		Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("You need a Description.");
		transaction.setDescription("Tim Hortons");
		
		error = Assertions.catchThrowable( () -> service.validate(transaction) );
		Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Give a valid month.");
		transaction.setMonth(3);
		
		error = Assertions.catchThrowable( () -> service.validate(transaction) );
		Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Give a valid year.");
		transaction.setYear(2020);

		error = Assertions.catchThrowable( () -> service.validate(transaction) );
		Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Inform an User.");
		User user = new User();
		user.setId(1l);
		transaction.setUser(user);
		
		error = Assertions.catchThrowable( () -> service.validate(transaction) );
		Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Inform a positive value.");
		transaction.setValue(BigDecimal.valueOf(5.98));
		
		error = Assertions.catchThrowable( () -> service.validate(transaction) );
		Assertions.assertThat(error).isInstanceOf(BusinessRuleException.class).hasMessage("Give the type of the transaction.");
		transaction.setType(TransactionType.EXPENSE);
	}
}
