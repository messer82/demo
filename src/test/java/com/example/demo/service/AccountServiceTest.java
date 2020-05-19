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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    public void test_delete_account_by_id() {
        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        when(accountRepository.findById(account1.getAccountId())).
                thenReturn(account1).
                thenThrow(AccountNotFoundException.class);

        accountService.deleteAccountById(account1.getAccountId());

        verify(accountRepository).deleteById(eq(account1.getAccountId()));
    }

    @Test
    public void test_get_accounts() {

        when(accountRepository.findAll()).
                thenReturn(accounts).
                thenThrow(AccountNotFoundException.class);

        List<Account> result = accountService.getAccounts();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(account1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(account2);

        verify(accountRepository).findAll();
    }

    @Test
    public void given_exception_when_get_accounts_then_account_not_found_exception_is_thrown() {
        when(accountRepository.findAll()).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> accountService.getAccounts());

        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No accounts available!");
    }

    @Test
    public void test_get_account_by_id() {
        int accountId = 15;

        account1 = Account.builder()
                .accountId(1)
                .userId(5)
                .accountNumber("RO454545")
                .balance(BigDecimal.valueOf(1234))
                .build();

        when(accountRepository.findById(anyInt())).
                thenReturn(account1);

        Account actualResult = accountService.getAccountById(accountId);

        assertThat(actualResult).isEqualToComparingFieldByField(account1);

        verify(accountRepository).findById(eq(accountId));
    }

    @Test
    public void given_exception_when_getting_account_by_id_then_account_not_found_exception_is_thrown() {
        int accountId = 15;

        account1 = Account.builder()
                .accountId(1)
                .userId(5)
                .accountNumber("RO454545")
                .balance(BigDecimal.valueOf(1234))
                .build();

        when(accountRepository.findById(anyInt())).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> accountService.getAccountById(accountId));

        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No account for this account id!");
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

        when(accountRepository.findByAccountNumber(anyString())).
                thenReturn(account1).
                thenThrow(AccountNotFoundException.class);

        Account result = accountService.getAccountByAccountNumber(accountNumber);

        assertThat(result).isEqualToComparingFieldByField(account1);

        verify(accountRepository).findByAccountNumber(eq(accountNumber));
    }

    @Test
    public void given_exception_when_get_account_by_account_number_then_account_not_found_exception_is_thrown( ){
        String accountNumber = "RO45454";

        when(accountRepository.findByAccountNumber(anyString())).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> accountService.getAccountByAccountNumber(accountNumber));

        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("There is no internal account for this account number!");
    }

    @Test
    public void test_create_account() throws SQLException {
        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        when(accountRepository.save(ArgumentMatchers.any())).
                thenReturn(account1).
                thenThrow(SQLException.class);

        Account result = accountService.createAccount(account1);

        assertThat(result).isEqualToComparingFieldByField(account1);

        verify(accountRepository).save(eq(account1));
    }

    @Test
    public void test_update_account_balance() {
        AccountPatch accountPatch = AccountPatch.builder().accountId(1).balance(BigDecimal.valueOf(2500)).build();

        accountService.updateAccountBalance(accountPatch);

        verify(accountRepository).updateAccount(anyInt(), any());
    }

    @Test
    public void given_exception_when_update_account_balance_then_account_not_found_exception_is_thrown() {
        AccountPatch accountPatch = AccountPatch.builder().accountId(5).balance(BigDecimal.valueOf(456)).build();

        when(accountRepository.updateAccount(anyInt(), any())).thenThrow(EmptyResultDataAccessException.class);

        Throwable throwable = Assertions.catchThrowable(() -> accountService.updateAccountBalance(accountPatch));

        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
        assertThat(throwable.getMessage()).isEqualTo("No valid account details for patch!");
    }

}
