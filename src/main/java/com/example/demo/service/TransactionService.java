package com.example.demo.service;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.TransactionPatch;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(int accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Transaction createTransaction(Transaction transaction) {
        BigDecimal initialBalance = accountRepository.findById(transaction.getAccount_id()).getBalance();
        accountRepository.findById(transaction.getAccount_id()).setBalance(initialBalance.subtract(transaction.getAmount_paid()));
        return transactionRepository.save(transaction);
    }

    public void deleteTransactionById(int id) {
        transactionRepository.deleteById(id);
    }

    //for recurrent payments to the same account (e.g. utility bill payment)
    public Transaction updatePartialTransaction(@Valid TransactionPatch transactionPatch) {
        Transaction transaction = transactionRepository.findById(transactionPatch.getTransaction_id());
        transaction.setAmount_paid(transactionPatch.getAmount_paid());
        return transactionRepository.save(transaction);
    }
}
