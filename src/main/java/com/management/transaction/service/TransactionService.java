package com.management.transaction.service;

import com.management.transaction.exception.InsufficientFundsException;
import com.management.transaction.exception.InvalidAccountException;
import com.management.transaction.model.Account;
import com.management.transaction.model.Transaction;
import com.management.transaction.model.TransactionType;
import com.management.transaction.repository.AccountRepository;
import com.management.transaction.repository.TransactionRepository;
import constant.MessageConstant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    public Account createAccount(String accountHolderName, BigDecimal initialBalance) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountHolderName(accountHolderName);
        account.setBalance(initialBalance);
        return accountRepository.save(account);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new InvalidAccountException(MessageConstant.INVALID_ACCOUNT + ": " + accountNumber));
    }

    @Transactional
    public void deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive!");
        }
        Account account = getAccountByAccountNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        logTransaction(account, "DEPOSIT", amount, "Deposit of ₹" + amount + " successful");
    }

    @Transactional
    public void withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive!");
        }
        Account account = getAccountByAccountNumber(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        logTransaction(account, "WITHDRAWAL", amount, "Withdrawal of ₹" + amount + " successful");
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive!");
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer funds to the same account.");
        }

        Account fromAccount = getAccountByAccountNumber(fromAccountNumber);
        Account toAccount = getAccountByAccountNumber(toAccountNumber);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for transfer.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);
        logTransaction(fromAccount, "TRANSFER_SENT", amount, "Transfer to account " + toAccountNumber + " successful");

        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);
        logTransaction(toAccount, "TRANSFER_RECEIVED", amount, "Transfer from account " + fromAccountNumber + " successful");
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void logTransaction(Account account, String type, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.valueOf(type.toUpperCase()));
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }

    @Transactional
    public List<Transaction> getTransactionHistory(String accountNumber) {
        if (accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Invalid account number!");
        }

        return transactionRepository.findByAccountOrderByTimestampDesc(getAccountByAccountNumber(accountNumber))
                .orElseThrow(() -> new InvalidAccountException("Transaction details not found with specified account number: " + accountNumber));
    }
}
