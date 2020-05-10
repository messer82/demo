package com.example.demo.repository;

import com.example.demo.domain.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void deleteById(int id) {
        String deleteStatement = "DELETE FROM accounts WHERE account_id = ?";
        int rowsDeleted = jdbcTemplate.update(deleteStatement, id);
    }

    @Override
    public List<Account> findAll() {
        String sqlQuery = "SELECT account_id, user_id, account_no, account_balance FROM accounts";
        RowMapper<Account> accountRowMapper = getAccountRowMapper();
        List<Account> accounts = jdbcTemplate.query(sqlQuery, accountRowMapper);
        Collections.sort(accounts, Comparator.comparing(Account::getAccountId));
        return accounts;
    }

    @Override
    public Account findById(int id) {
        String sqlQuery = "SELECT account_id, user_id, account_no, account_balance FROM accounts WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, getAccountRowMapper(), id);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        String sqlQuery = "SELECT account_id, user_id, account_no, account_balance FROM accounts WHERE account_no = ?";
        return jdbcTemplate.queryForObject(sqlQuery, getAccountRowMapper(), accountNumber);
    }

    @Override
    public Account save(Account account) throws SQLException {
        String sqlQuery = "INSERT INTO accounts (user_id, account_no, account_balance) VALUES (?, ?, ?)";

        jdbcTemplate.update(sqlQuery, account.getUserId(), account.getAccountNumber(), account.getBalance());
        return findAll().stream().max(Comparator.comparing(Account::getAccountId)).get();
    }

    @Override
    public Account updateAccount(int id, BigDecimal balance) {
        String sqlUpdate = "UPDATE accounts SET account_balance = ? WHERE account_id = ?";

        jdbcTemplate.update(sqlUpdate, balance, id);

        return findById(id);
    }

    private RowMapper<Account> getAccountRowMapper() {
        return ((rs, rowNum) -> {
            Account account = new Account();
            account.setAccountId(rs.getInt("account_id"));
            account.setUserId(rs.getInt("user_id"));
            account.setAccountNumber(rs.getString("account_no"));
            account.setBalance(rs.getBigDecimal("account_balance"));
            return account;
        });
    }
}
