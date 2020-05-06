package com.example.demo.controller;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        accountService.deleteAccountById(account_id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/account_id/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountById(@PathVariable(name = "account_id") int account_id) {
        return accountService.getAccountById(account_id);
    }

    @GetMapping("/{account_no}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountByAccountNumber(@PathVariable(name = "account_no") String account_no) {
        return accountService.getAccountByAccountNumber(account_no);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody @Valid Account account) throws SQLException {
        return accountService.createAccount(account);
    }

    @PatchMapping("/{account_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account updateAccount(@PathVariable int account_id, @RequestBody AccountPatch accountPatch) {
        accountPatch.setAccountId(account_id);
        return accountService.updateAccountBalance(accountPatch);
    }

}
