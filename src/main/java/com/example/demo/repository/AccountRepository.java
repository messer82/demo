package com.example.demo.repository;

import com.example.demo.domain.entity.Account;

import java.util.List;

public interface AccountRepository {
    void deleteById(int id);

    List<Account> findAll();

    Account findById(int id);

    Account save(Account account);

}
