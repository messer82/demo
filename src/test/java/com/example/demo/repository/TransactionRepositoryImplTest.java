package com.example.demo.repository;

import com.example.demo.domain.entity.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class TransactionRepositoryImplTest {

    private Transaction transaction1 = null;
    private Transaction transaction2 = null;

    @InjectMocks
    private TransactionRepositoryImpl transactionRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;

    List<Transaction> transactions = new ArrayList<>();

    @Before
    public void setup() {
        transaction1 = Transaction.
                builder().
                transactionId(1).
                accountId(1).
                destinationAccount("RO454545").
                amount(BigDecimal.valueOf(1212)).
                transactionDate(LocalDateTime.parse("2000-01-01T10:15:30")).
                build();
        transaction2 = Transaction.
                builder().
                transactionId(2).
                accountId(2).
                destinationAccount("RO121212").
                amount(BigDecimal.valueOf(4545)).
                transactionDate(LocalDateTime.parse("2001-01-01T10:15:30")).
                build();

        transactions.add(transaction1);
        transactions.add(transaction2);
    }

    @Test
    public void test_find_all() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).
                thenReturn(transactions);

        List<Transaction> result = transactionRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(transaction1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(transaction2);
    }

    @Test
    public void test_find_by_account_id() {

        transactionRepository.findByAccountId(transaction1.getAccountId());

        verify(jdbcTemplate).
                query(anyString(), any(RowMapper.class), eq(transaction1.getAccountId()));
    }

    @Test
    public void test_find_by_id() {

        transactionRepository.findById(transaction1.getAccountId());

        verify(jdbcTemplate).
                queryForObject(anyString(), any(RowMapper.class), eq(transaction1.getAccountId()));
    }

    @Test
    public void test_save() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).
                thenReturn(transactions);

        transaction1 = Transaction.
                builder().
                transactionId(1).
                accountId(1).
                destinationAccount("RO454545").
                amount(BigDecimal.valueOf(1212)).
                transactionDate(LocalDateTime.parse("2000-01-01T10:15:30")).
                build();

        Transaction response = transactionRepository.save(transaction1);

        verify(jdbcTemplate).update(anyString(), anyInt(), anyString(), any());

        assertThat(response).isEqualToComparingFieldByField(transaction2);
    }

    @Test
    public void test_delete_by_id() {

        transactionRepository.deleteById(transaction1.getTransactionId());

        verify(jdbcTemplate).update(anyString(), eq(transaction1.getTransactionId()));
    }
}
