package com.example.demo.repository;

import com.example.demo.domain.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Transaction> findAll() {
        String sqlQuery = "SELECT transaction_id, account_id, destination_account, amount_paid, transaction_date FROM transactions";
        RowMapper<Transaction> transactionRowMapper = getTransactionRowMapper();
        List<Transaction> transactions = jdbcTemplate.query(sqlQuery, transactionRowMapper);
        Collections.sort(transactions, Comparator.comparing(Transaction::getTransaction_id));

        return transactions;
    }

    @Override
    public List<Transaction> findByAccountId(int id) {
        String sqlQuery = "SELECT transaction_id, account_id, destination_account, amount_paid, transaction_date FROM transactions WHERE account_id = ?";
        RowMapper<Transaction> transactionRowMapper = getTransactionRowMapper();
        List<Transaction> transactionsById = jdbcTemplate.query(sqlQuery, transactionRowMapper, id);
        Collections.sort(transactionsById, Comparator.comparing(Transaction::getAccount_id));

        return transactionsById;
    }

    @Override
    public Transaction findById(int id) {
        String sqlQuery = "SELECT transaction_id, account_id, destination_account, amount_paid, transaction_date FROM transactions WHERE transaction_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, getTransactionRowMapper(), id);
    }

    private RowMapper<Transaction> getTransactionRowMapper() {
        return ((rs, rowNum) -> {
            Transaction transaction = new Transaction();
            transaction.setTransaction_id(rs.getInt("transaction_id"));
            transaction.setAccount_id(rs.getInt("account_id"));
            transaction.setDestination_account(rs.getString("destination_account"));
            transaction.setAmount_paid(rs.getDouble("amount_paid"));
            transaction.setTransaction_date(rs.getDate("transaction_date").toLocalDate());
            return transaction;
        });
    }
}
