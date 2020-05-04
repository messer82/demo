package com.example.demo.repository;

import com.example.demo.domain.entity.Transaction;

import java.util.List;

public interface TransactionRepository {
    List<Transaction> findAll();

    List<Transaction> findByAccountId(int id);

    Transaction findById(int id);

    Transaction save(Transaction transaction);

//    Transaction update(int id, BigDecimal amount, LocalDate date);

    void deleteById(int id);
}
