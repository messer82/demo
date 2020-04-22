package com.example.demo.service;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.TransactionPatch;
import com.example.demo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Service

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(int accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.findById(transactionId);
    }

    //for recurrent payments to the same account (e.g. utility bill payment)
    public Transaction updatePartialTransaction(@Valid TransactionPatch transactionPatch) {
        Transaction transaction = transactionRepository.findById(transactionPatch.getTransaction_id());
        transaction.setAmount_paid(transactionPatch.getAmount_paid());
        transaction.setTransaction_date(transactionPatch.getTransaction_date());
        return transactionRepository.save(transaction);
    }
}
