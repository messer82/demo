package com.example.demo.repository;

import com.example.demo.domain.entity.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class))).
                thenReturn(transactions);

        List<Transaction> result = transactionRepository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(transaction1);
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(transaction2);
    }

    @Test
    public void test_find_by_account_id() {

        transactionRepository.findByAccountId(transaction1.getAccountId());

        Mockito.verify(jdbcTemplate).
                query(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.eq(transaction1.getAccountId()));
    }

    @Test
    public void test_find_by_id() {

        transactionRepository.findById(transaction1.getAccountId());

        Mockito.verify(jdbcTemplate).
                queryForObject(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.eq(transaction1.getAccountId()));
    }

    @Test
    public void test_save() {
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class))).
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

        Mockito.verify(jdbcTemplate).
                update(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.any());

        Assertions.assertThat(response).isEqualToComparingFieldByField(transaction2);
    }

    @Test
    public void test_delete_by_id() {

        transactionRepository.deleteById(transaction1.getTransactionId());

        Mockito.verify(jdbcTemplate).update(Mockito.anyString(), Mockito.eq(transaction1.getTransactionId()));
    }
}
