package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Service

public class AccountService {

    private final AccountRepository accountRepository;

    public void deleteAccountById(int id) {
        accountRepository.deleteById(id);
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updatePartialAccount(@Valid AccountPatch accountPatch) {
        Account account = accountRepository.findById(accountPatch.getAccountId());
        account.setBalance(accountPatch.getBalance());
        return accountRepository.update(accountPatch.getAccountId(), accountPatch.getBalance());
    }
}
