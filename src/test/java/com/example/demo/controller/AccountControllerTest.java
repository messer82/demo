package com.example.demo.controller;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)

public class AccountControllerTest {

    private Account account = null;

    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountService accountService;

    @Before
    public void setup() {
        account = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();
    }

    @Test
    public void test_delete_account_by_id() {
        accountController.deleteAccountById(Mockito.anyInt());

        Mockito.verify(accountService).deleteAccountById(Mockito.anyInt());
    }

    @Test
    public void test_get_accounts() {
        accountController.getAccounts();

        Mockito.verify(accountService).getAccounts();
    }

    @Test
    public void test_get_account_by_id() {
        accountController.getAccountById(Mockito.anyInt());

        Mockito.verify(accountService).getAccountById(Mockito.anyInt());
    }

    @Test
    public void test_get_account_by_account_number() {
        accountController.getAccountByAccountNumber(Mockito.anyString());

        Mockito.verify(accountService).getAccountByAccountNumber(Mockito.anyString());
    }

    @Test
    public void test_create_account() throws SQLException {
        account = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        accountController.createAccount(account);

        Mockito.verify(accountService).createAccount(Mockito.eq(account));
    }

    @Test
    public void test_update_account() {
        account = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        AccountPatch accountPatch = AccountPatch.builder().accountId(1).balance(BigDecimal.valueOf(5000)).build();

        accountController.updateAccount(account.getAccountId(), accountPatch);

        Mockito.verify(accountService).updateAccountBalance(accountPatch);
    }
}
