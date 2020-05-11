package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.TransactionPatch;
import com.example.demo.repository.AccountRepositoryImpl;
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

@RunWith(MockitoJUnitRunner.class)

public class TransactionServiceTest {

    private Transaction transaction1 = null;
    private Account account1 = null;

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionRepositoryImpl transactionRepository;

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

        account1 = Account.
                builder().
                accountId(1).
                userId(5).
                accountNumber("RO454545").
                balance(BigDecimal.valueOf(1234)).build();
    }

    @Test
    public void test_get_all_transactions() {
        transactionService.getAllTransactions();

        Mockito.verify(transactionRepository).findAll();
    }

    @Test
    public void test_get_transactions_by_account() {
        int accountId = 10;

        transactionService.getTransactionsByAccount(accountId);

        Mockito.verify(transactionRepository).findByAccountId(Mockito.eq(accountId));
    }

    @Test
    public void test_get_transaction_by_id() {
        int transactionId = 2;

        transactionService.getTransactionById(transactionId);

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

        Mockito.when(accountService.getAccountById(transaction1.getAccountId())).thenReturn(account1);

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

        Mockito.when(transactionRepository.findById(transaction1.getTransactionId())).thenReturn(transaction1);

        transactionService.deleteTransactionById(transaction1.getTransactionId());

        Mockito.verify(transactionRepository).deleteById(Mockito.eq(transaction1.getTransactionId()));
    }
}
