package com.example.demo.repository;

import com.example.demo.domain.entity.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionRepository {
    List<Transaction> findAll();

    List<Transaction> findByAccountId(int id);

    Transaction findById(int id);

    Transaction save(Transaction transaction);

    void deleteById(int id);
}
