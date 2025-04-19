package com.management.transaction.controller;

import com.management.transaction.dto.AccountCreationRequest;
import com.management.transaction.model.Account;
import com.management.transaction.model.Transaction;
import com.management.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        Account newAccount = transactionService.createAccount(request.getAccountHolderName(), request.getInitialBalance());
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = transactionService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        transactionService.deposit(accountNumber, amount);
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/{accountNumber}//withdraw")
    public ResponseEntity<String> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        transactionService.withdraw(accountNumber, amount);
        return ResponseEntity.ok("Withdrawal successful");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam BigDecimal amount) {
        transactionService.transfer(fromAccount, toAccount, amount);
        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber) {
        List<Transaction> transactionHistory = transactionService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(transactionHistory);
    }
}
