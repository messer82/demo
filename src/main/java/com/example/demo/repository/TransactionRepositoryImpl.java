package com.example.demo.repository;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Transaction> findAll() {
            String sqlQuery = "SELECT transaction_id, account_id, destination_account, amount, transaction_date FROM transactions";
            RowMapper<Transaction> transactionRowMapper = getTransactionRowMapper();
            List<Transaction> transactions = jdbcTemplate.query(sqlQuery, transactionRowMapper);
            Collections.sort(transactions, Comparator.comparing(Transaction::getTransactionId));

            return transactions;
    }

    @Override
    public List<Transaction> findByAccountId(int id) {
            String sqlQuery = "SELECT transaction_id, account_id, destination_account, amount, transaction_date FROM transactions WHERE account_id = ?";
            RowMapper<Transaction> transactionRowMapper = getTransactionRowMapper();
            List<Transaction> transactionsById = jdbcTemplate.query(sqlQuery, transactionRowMapper, id);
            Collections.sort(transactionsById, Comparator.comparing(Transaction::getAccountId));

            return transactionsById;
    }

    @Override
    public Transaction findById(int id) {
            String sqlQuery = "SELECT transaction_id, account_id, destination_account, amount, transaction_date FROM transactions WHERE transaction_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, getTransactionRowMapper(), id);
    }

    @Override
    public Transaction save(Transaction transaction) {
        try {
            String sqlQuery = "INSERT INTO transactions (account_id, destination_account, amount) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    transaction.getAccountId(),
                    transaction.getDestinationAccount(),
                    transaction.getAmount());
            return findAll().stream().max(Comparator.comparing(Transaction::getTransactionId)).get();
        } catch (DataIntegrityViolationException exception) {
            throw new AccountNotFoundException("Creation of transaction went wrong!");
        }
    }

    @Override
    public void deleteById(int id) {
            String sqlQuery = "DELETE FROM transactions WHERE transaction_id = ?";
            jdbcTemplate.update(sqlQuery, id);
    }

    private RowMapper<Transaction> getTransactionRowMapper() {
        return ((rs, rowNum) -> {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(rs.getInt("transaction_id"));
            transaction.setAccountId(rs.getInt("account_id"));
            transaction.setDestinationAccount(rs.getString("destination_account"));
            transaction.setAmount(rs.getBigDecimal("amount"));
            transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
            return transaction;
        });
    }
}
