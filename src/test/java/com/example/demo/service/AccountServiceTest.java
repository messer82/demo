package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.repository.AccountRepositoryImpl;
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

public class AccountServiceTest {

    private Account account1 = null;
    private Account account2 = null;

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepositoryImpl accountRepository;

    @Before
    public void setup() {
        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();
    }

    @Test
    public void test_delete_account_by_id(){
        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        Mockito.when(accountRepository.findById(account1.getAccountId())).thenReturn(account1);

        accountService.deleteAccountById(account1.getAccountId());

        Mockito.verify(accountRepository).deleteById(Mockito.eq(account1.getAccountId()));
    }

    @Test
    public void test_get_accounts() {
        accountService.getAccounts();

        Mockito.verify(accountRepository).findAll();
    }

    @Test
    public void test_get_account_by_id() {
        int accountId = 15;

        accountService.getAccountById(accountId);

        Mockito.verify(accountRepository).findById(Mockito.eq(accountId));
    }

    @Test
    public void test_get_account_by_account_number() {
        String accountNumber = "RO6598";

        accountService.getAccountByAccountNumber(accountNumber);

        Mockito.verify(accountRepository).findByAccountNumber(Mockito.eq(accountNumber));
    }

    @Test
    public void test_create_account() throws SQLException {
        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        accountService.createAccount(account1);

        Mockito.verify(accountRepository).save(Mockito.eq(account1));
    }

    @Test
    public void test_update_account_balance() {
        AccountPatch accountPatch = AccountPatch.builder().accountId(1).balance(BigDecimal.valueOf(2500)).build();

        accountService.updateAccountBalance(accountPatch);

        Mockito.verify(accountRepository).updateAccount(Mockito.anyInt(), Mockito.any());
    }

}
