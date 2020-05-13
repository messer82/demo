package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.repository.AccountRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)

public class AccountServiceTest {

    private Account account1 = null;
    private Account account2 = null;

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepositoryImpl accountRepository;

    List<Account> accounts = new ArrayList<>();

    @Before
    public void setup() {
        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        account2 = Account.
                builder().
                accountId(2).
                userId(6).
                accountNumber("RO454789").
                balance(BigDecimal.valueOf(1255)).build();

        accounts.add(account1);
        accounts.add(account2);
    }

    @Test
    public void test_delete_account_by_id(){
        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        Mockito.when(accountRepository.findById(account1.getAccountId())).
                thenReturn(account1).
                thenThrow(AccountNotFoundException.class);

        accountService.deleteAccountById(account1.getAccountId());

        Mockito.verify(accountRepository).deleteById(Mockito.eq(account1.getAccountId()));
    }

    @Test
    public void test_get_accounts() {

        Mockito.when(accountRepository.findAll()).
                thenReturn(accounts).
                thenThrow(AccountNotFoundException.class);

        List<Account> result = accountService.getAccounts();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(account1);
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(account2);

        Mockito.verify(accountRepository).findAll();
    }

    @Test
    public void test_get_account_by_id() {
        int accountId = 15;

        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        Mockito.when(accountRepository.findById(ArgumentMatchers.anyInt())).
                thenReturn(account1).
                thenThrow(AccountNotFoundException.class);

        Account actualResult = accountService.getAccountById(accountId);

        Assertions.assertThat(actualResult).isEqualToComparingFieldByField(account1);

        Mockito.verify(accountRepository).findById(Mockito.eq(accountId));
    }

    @Test
    public void test_get_account_by_account_number() {
        String accountNumber = "RO6598";

        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        Mockito.when(accountRepository.findByAccountNumber(ArgumentMatchers.anyString())).
                thenReturn(account1).
                thenThrow(AccountNotFoundException.class);

        Account result = accountService.getAccountByAccountNumber(accountNumber);

        Assertions.assertThat(result).isEqualToComparingFieldByField(account1);

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

        Mockito.when(accountRepository.save(ArgumentMatchers.any())).
                thenReturn(account1).
                thenThrow(SQLException.class);

        Account result = accountService.createAccount(account1);

        Assertions.assertThat(result).isEqualToComparingFieldByField(account1);

        Mockito.verify(accountRepository).save(Mockito.eq(account1));
    }

    @Test
    public void test_update_account_balance() {
        AccountPatch accountPatch = AccountPatch.builder().accountId(1).balance(BigDecimal.valueOf(2500)).build();

        accountService.updateAccountBalance(accountPatch);

        Mockito.verify(accountRepository).updateAccount(Mockito.anyInt(), Mockito.any());
    }

}
