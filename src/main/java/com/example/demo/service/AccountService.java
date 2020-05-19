package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Service

public class AccountService {

    private final AccountRepository accountRepository;

    public void deleteAccountById(int id) {
//        check that account exists, else throw AccountNotFoundException
        Account account = getAccountById(id);

        accountRepository.deleteById(id);
    }

    public List<Account> getAccounts() {
        try {
            return accountRepository.findAll();
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("No accounts available!");
        }
    }

    public Account getAccountById(int id) {
        try {
            return accountRepository.findById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("No account for this account id!");
        }
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        try {
            return accountRepository.findByAccountNumber(accountNumber);
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("There is no internal account for this account number!");
        }
    }

    public Account createAccount(Account account) throws SQLException {
            return accountRepository.save(account);
    }

//    this is for top up the account balance
    public Account updateAccountBalance(@Valid AccountPatch accountPatch) {
        try{
        return accountRepository.updateAccount(accountPatch.getAccountId(), accountPatch.getBalance());
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("No valid account details for patch!");
        }
    }
}
