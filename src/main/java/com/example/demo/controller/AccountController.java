package com.example.demo.controller;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @DeleteMapping("/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccountById(@PathVariable(name = "account_id") int account_id) {
        if (getAccountById(account_id).getAccount_id() >  0) {
            accountService.deleteAccountById(account_id);
        } else  {
            throw new AccountNotFoundException();
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/account_id/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountById(@PathVariable(name = "account_id") int account_id) {
        try {
            return accountService.getAccountById(account_id);
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("No account for this Id!");
        }
    }

    @GetMapping("/{account_no}")
    @ResponseStatus(HttpStatus.OK)
        public Account getAccountByAccountNumber(@PathVariable(name = "account_no") String account_no) {
        try {
            return accountService.getAccountByAccountNumber(account_no);
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("No account for this account number!");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody @Valid Account account) {
        try {
            return accountService.createAccount(account);
        } catch (DataIntegrityViolationException exception) {
            throw new UserNotFoundException("No valid user Id to create account!");
        }
    }

    @PatchMapping("/{account_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account updateAccount(@PathVariable int account_id, @RequestBody AccountPatch accountPatch) {
        accountPatch.setAccountId(account_id);
        return accountService.updateAccountBalance(accountPatch);
    }

}
