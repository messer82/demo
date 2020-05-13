package com.example.demo.repository;

import com.example.demo.domain.entity.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class AccountRepositoryImplTest {

    private Account account1 = null;
    private Account account2 = null;

    @InjectMocks
    private AccountRepositoryImpl accountRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;

    List<Account> accounts = new ArrayList<>();

    @Before
    public void setup() {
        account1 = Account.
                builder().
                accountId(1).
                userId(1).
                accountNumber("RO455454").
                balance(BigDecimal.valueOf(4545)).
                build();
        account2 = Account.
                builder().
                accountId(2).
                userId(2).
                accountNumber("RO784886").
                balance(BigDecimal.valueOf(4532)).
                build();
        accounts.add(account1);
        accounts.add(account2);
    }

    @Test
    public void test_delete_by_id() {

        accountRepository.deleteById(account1.getAccountId());

        verify(jdbcTemplate).update(anyString(), eq(account1.getAccountId()));
    }

    @Test
    public void test_find_all() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(accounts);

        List<Account> result = accountRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(account1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(account2);
    }

    @Test
    public void test_find_by_id() {

        accountRepository.findById(account1.getAccountId());

        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(account1.getAccountId()));
    }

    @Test
    public void test_find_by_account_number() {

        accountRepository.findByAccountNumber(account1.getAccountNumber());

        verify(jdbcTemplate).queryForObject(anyString(), any(RowMapper.class), eq(account1.getAccountNumber()));
    }

    @Test
    public void test_save() throws SQLException {

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(accounts);

        account1 = Account.
                builder().
                accountId(1).
                userId(1).
                accountNumber("RO455454").
                balance(BigDecimal.valueOf(4545)).
                build();

        Account response = accountRepository.save(account1);

        verify(jdbcTemplate).update(anyString(), anyInt(), anyString(), any());

        assertThat(response).isEqualToComparingFieldByField(account2);
    }

    @Test
    public void test_update_account() {

        accountRepository.updateAccount(account1.getAccountId(), account1.getBalance());

        verify(jdbcTemplate).update(anyString(), any(), anyInt());
    }
}
