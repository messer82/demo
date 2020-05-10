package com.example.demo.repository;

import com.example.demo.domain.entity.Account;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface AccountRepository {
    void deleteById(int id);

    List<Account> findAll();

    Account findById(int id);

    Account findByAccountNumber(String accountNumber);

    Account save(Account account) throws SQLException;

    Account updateAccount(int accountId, BigDecimal balance);
}
