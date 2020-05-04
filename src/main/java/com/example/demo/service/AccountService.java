package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.BalanceTooLowException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service

public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public void deleteAccountById(int id) {
        accountRepository.deleteById(id);
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        try {
            return accountRepository.findByAccountNumber(accountNumber);
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("There is no account for this account number!");
        }
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccountBalance(@Valid AccountPatch accountPatch) {
        Account account = accountRepository.findById(accountPatch.getAccountId());
        account.setBalance(accountPatch.getBalance());
        return accountRepository.update(accountPatch.getAccountId(), accountPatch.getBalance());
    }
}
