package com.management.transaction.repository;

import com.management.transaction.model.Account;
import com.management.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOrderByTimestampDesc(Account account);
    List<Transaction> findByAccountId(Long accountId);
}
