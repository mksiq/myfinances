package com.mck.personalfinancer.model.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mck.personalfinancer.model.entity.Transaction;
import com.mck.personalfinancer.model.enums.TransactionStatus;
import com.mck.personalfinancer.model.enums.TransactionType;
import com.mck.personalfinancer.repository.TransactionRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TransactionRepositoryTest {

	@Autowired
	TransactionRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void shallInsertOneTransaction() {
		Transaction transaction = createTransaction();
		transaction = repository.save(transaction);

		Assertions.assertThat(transaction.getId()).isNotNull();
	}

	@Test
	public void shallDeleteOneTransaction() {
		Transaction transaction = persistTransaction();
		transaction = entityManager.find(Transaction.class, transaction.getId());
		repository.delete(transaction);
		Transaction deletedTransaction = entityManager.find(Transaction.class, transaction.getId());

		Assertions.assertThat(deletedTransaction).isNull();
	}

	@Test
	public void shallUpdateOneTransaction() {
		Transaction transaction = persistTransaction();
		transaction.setDescription("Starbucks");
		transaction.setValue(BigDecimal.valueOf(30.13));
		transaction.setStatus(TransactionStatus.CANCELED);
		repository.save(transaction);
		transaction = entityManager.find(Transaction.class, transaction.getId());

		Transaction updatedTransaction = entityManager.find(Transaction.class, transaction.getId());

		Assertions.assertThat(updatedTransaction.getDescription()).isEqualTo("Starbucks");
		Assertions.assertThat(updatedTransaction.getValue()).isEqualTo(BigDecimal.valueOf(30.13));
		Assertions.assertThat(updatedTransaction.getStatus()).isEqualTo(TransactionStatus.CANCELED);
	}

	@Test
	public void shallGetOneTransactionById() {
		Transaction transaction = persistTransaction();
		Optional<Transaction> foundTransaction = repository.findById(transaction.getId());

		Assertions.assertThat(foundTransaction.isPresent()).isTrue();
	}

	public static Transaction createTransaction() {
		return new Transaction(null, "Tim Hortons", 2, 2020, null, BigDecimal.valueOf(10.23), null,
				TransactionType.EXPENSE, TransactionStatus.PENDING);
	}

	private Transaction persistTransaction() {
		Transaction transaction = createTransaction();
		entityManager.persist(transaction);
		return transaction;
	}
}
