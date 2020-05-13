package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.repository.TransactionRepositoryImpl;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)

public class TransactionServiceTest {

    private Transaction transaction1 = null;
    private Transaction transaction2 = null;
    private Account account1 = null;

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepositoryImpl transactionRepository;

    List<Transaction> transactions = new ArrayList<>();

    @Before
    public void setup() {
        transaction1 = Transaction.
                builder().
                transactionId(1).
                accountId(2).
                destinationAccount("RO78955").
                amount(BigDecimal.valueOf(445)).
                transactionDate(LocalDateTime.parse("2020-01-02T09:10:10")).
                build();

        transaction2 = Transaction.
                builder().
                transactionId(2).
                accountId(3).
                destinationAccount("RO78978").
                amount(BigDecimal.valueOf(4452)).
                transactionDate(LocalDateTime.parse("2020-01-03T09:10:10")).
                build();

        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        transactions.add(transaction1);
        transactions.add(transaction2);
    }

    @Test
    public void test_get_all_transactions() {

        Mockito.when(transactionRepository.findAll()).
                thenReturn(transactions).
                thenThrow(TransactionNotFoundException.class);

        List<Transaction> result = transactionService.getAllTransactions();

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0)).isEqualToComparingFieldByField(transaction1);
        Assertions.assertThat(result.get(1)).isEqualToComparingFieldByField(transaction2);

        Mockito.verify(transactionRepository).findAll();
    }

    @Test
    public void test_get_transactions_by_account() {
        int accountId = 10;

        Mockito.when(transactionRepository.findByAccountId(ArgumentMatchers.anyInt())).
                thenReturn(transactions).
                thenThrow(TransactionNotFoundException.class);

        List<Transaction> result = transactionService.getTransactionsByAccount(accountId);

        Assertions.assertThat(result.size()).isEqualTo(transactions.size());

        Mockito.verify(transactionRepository).findByAccountId(Mockito.eq(accountId));
    }

    @Test
    public void test_get_transaction_by_id() {
        int transactionId = 2;

        transaction1 = Transaction.
                builder().
                transactionId(1).
                accountId(2).
                destinationAccount("RO78955").
                amount(BigDecimal.valueOf(445)).
                transactionDate(LocalDateTime.parse("2020-01-02T09:10:10")).
                build();

        Mockito.when(transactionRepository.findById(ArgumentMatchers.anyInt())).
                thenReturn(transaction1).
                thenThrow(TransactionNotFoundException.class);

        Transaction result = transactionService.getTransactionById(transactionId);

        Assertions.assertThat(result).isEqualToComparingFieldByField(transaction1);

        Mockito.verify(transactionRepository).findById(Mockito.eq(transactionId));
    }

    @Test
    public void test_create_transaction() {

        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();

        transaction1 = Transaction.
                builder().
                transactionId(1).
                accountId(2).
                destinationAccount("RO78955").
                amount(BigDecimal.valueOf(445)).
                transactionDate(LocalDateTime.parse("2020-01-02T09:10:10")).
                build();

        Mockito.when(accountService.getAccountById(transaction1.getAccountId())).
                thenReturn(account1).
                thenThrow(AccountNotFoundException.class);

        transactionService.createTransaction(transaction1);

        Mockito.verify(transactionRepository).save(Mockito.eq(transaction1));
    }

    @Test
    public void test_delete_transaction_by_id() {
        transaction1 = Transaction.
                builder().
                transactionId(1).
                accountId(2).
                destinationAccount("RO78955").
                amount(BigDecimal.valueOf(445)).
                transactionDate(LocalDateTime.parse("2020-01-02T09:10:10")).
                build();

        Mockito.when(transactionRepository.findById(transaction1.getTransactionId())).
                thenReturn(transaction1).
                thenThrow(TransactionNotFoundException.class);

        transactionService.deleteTransactionById(transaction1.getTransactionId());

        Mockito.verify(transactionRepository).deleteById(Mockito.eq(transaction1.getTransactionId()));
    }
}
