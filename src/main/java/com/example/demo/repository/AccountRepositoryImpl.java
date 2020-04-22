package com.example.demo.repository;

import com.example.demo.domain.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
        int rowsDeleted = jdbcTemplate.update(deleteStatement);
    }

    @Override
    public List<Account> findAll() {
        String sqlQuery = "SELECT account_id, user_id, account_no, account_balance FROM accounts";
        RowMapper<Account> accountRowMapper = getAccountRowMapper();
        List<Account> accounts = jdbcTemplate.query(sqlQuery, accountRowMapper);
        Collections.sort(accounts, Comparator.comparing(Account::getAccount_id));
        return accounts;
    }

    @Override
    public Account findById(int id) {
        String sqlQuery = "SELECT account_id, user_id, account_no, account_balance FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, getAccountRowMapper(), id);
    }

    @Override
    public Account save(Account account) {
        String sqlQuery = "INSERT INTO accounts (user_id, account_no, account_balance) VALUES (?, ?, ?)";

        Object[] params = new Object[] {
            account.getUser_id(),
            account.getAccountNumber(),
            account.getBalance()};
        jdbcTemplate.update(sqlQuery, params);
        return findAll().stream().max(Comparator.comparing(Account::getAccount_id)).get();
    }

    private RowMapper<Account> getAccountRowMapper() {
        return ((rs, rowNum) -> {
            Account account = new Account();
            account.setAccount_id(rs.getInt("account_id"));
            account.setUser_id(rs.getInt("user_id"));
            account.setAccountNumber(rs.getString("account_no"));
            account.setBalance(rs.getBigDecimal("account_balance"));
            return account;
        });
    }
}
