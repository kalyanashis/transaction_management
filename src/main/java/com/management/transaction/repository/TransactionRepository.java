package com.management.transaction.repository;

import com.management.transaction.model.Account;
import com.management.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByAccountOrderByTimestampDesc(Account account);
}
