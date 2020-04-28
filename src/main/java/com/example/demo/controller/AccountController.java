package com.example.demo.controller;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.AccessControlException;
import java.sql.SQLDataException;
import java.sql.SQLException;
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

    @GetMapping("/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountById(@PathVariable(name = "account_id") int id) {
        try {
            return accountService.getAccountById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("No account for this Id!");
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
    public Account updateAccount(@PathVariable int account_id,@RequestBody AccountPatch accountPatch) {
        accountPatch.setAccountId(account_id);
        return accountService.updatePartialAccount(accountPatch);
    }

}
