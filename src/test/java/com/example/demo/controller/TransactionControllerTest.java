package com.example.demo.controller;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)

public class TransactionControllerTest {

    private Transaction transaction = null;

    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;

    @Before
    public void setup() {
        transaction = Transaction.
                builder().
                transactionId(1).
                accountId(2).
                destinationAccount("RO78955").
                amount(BigDecimal.valueOf(445)).
                transactionDate(LocalDateTime.parse("2020-01-02T09:10:10")).
                build();
    }

    @Test
    public void test_get_transactions() {
        transactionController.getTransactions();

        Mockito.verify(transactionService).getAllTransactions();
    }

    @Test
    public void test_get_transactions_by_account_id() {
        transactionController.getTransactionsByAccountId(Mockito.anyInt());

        Mockito.verify(transactionService).getTransactionsByAccount(Mockito.anyInt());
    }

    @Test
    public void test_get_transaction_by_id() {
        transactionController.getTransactionById(Mockito.anyInt());

        Mockito.verify(transactionService).getTransactionById(Mockito.anyInt());
    }

    @Test
    public void test_make_transaction() {
        transaction = Transaction.
                builder().
                transactionId(1).
                accountId(2).
                destinationAccount("RO78955").
                amount(BigDecimal.valueOf(445)).
                transactionDate(LocalDateTime.parse("2020-01-02T09:10:10")).
                build();

        transactionController.makeTransaction(transaction);

        Mockito.verify(transactionService).createTransaction(Mockito.eq(transaction));
    }

    @Test
    public void test_delete_transaction_by_id() {
        transactionController.deleteTransactionById(Mockito.anyInt());

        Mockito.verify(transactionService).deleteTransactionById(Mockito.anyInt());
    }
}
