package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.exception.AmountTooLargeException;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.repository.TransactionRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        when(transactionRepository.findAll()).
                thenReturn(transactions).
                thenThrow(TransactionNotFoundException.class);

        List<Transaction> result = transactionService.getAllTransactions();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(transaction1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(transaction2);

        verify(transactionRepository).findAll();
    }

    @Test
    public void test_get_transactions_by_account() {
        int accountId = 10;

        when(transactionRepository.findByAccountId(anyInt())).
                thenReturn(transactions).
                thenThrow(TransactionNotFoundException.class);

        List<Transaction> result = transactionService.getTransactionsByAccount(accountId);

        assertThat(result.size()).isEqualTo(transactions.size());

        verify(transactionRepository).findByAccountId(eq(accountId));
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

        when(transactionRepository.findById(anyInt())).
                thenReturn(transaction1).
                thenThrow(TransactionNotFoundException.class);

        Transaction result = transactionService.getTransactionById(transactionId);

        assertThat(result).isEqualToComparingFieldByField(transaction1);

        verify(transactionRepository).findById(eq(transactionId));
    }

    @Test
    public void given_amount_grater_then_2000_when_creating_transaction_then_amount_too_large_exception_is_thrown() {

        account1 = Account.builder()
                .accountId(1)
                .userId(5)
                .accountNumber("RO454545")
                .balance(BigDecimal.valueOf(1234))
                .build();

        transaction1 = Transaction.builder()
                .transactionId(1)
                .accountId(2)
                .destinationAccount("RO78955")
                .amount(BigDecimal.valueOf(3000))
                .transactionDate(LocalDateTime.parse("2020-01-02T09:10:10"))
                .build();

        when(accountService.getAccountById(transaction1.getAccountId())).thenReturn(account1);

        Throwable throwable = Assertions.catchThrowable(() -> transactionService.createTransaction(transaction1));

        assertThat(throwable).isInstanceOf(AmountTooLargeException.class);
        assertThat(throwable.getMessage()).isEqualTo("Transaction limit is of 2000!");
    }

    @Test
    public void given_destination_account_is_from_same_bank_when_creating_transaction_then_both_accounts_are_updated() {

        account1 = Account.builder()
                .accountId(1)
                .userId(5)
                .accountNumber("RO454545")
                .balance(BigDecimal.valueOf(1234))
                .build();

        Account accountByNumber = Account.builder()
                .accountId(2)
                .userId(6)
                .accountNumber("RO565656")
                .balance(BigDecimal.valueOf(1111))
                .build();

        transaction1 = Transaction.builder()
                .accountId(1)
                .destinationAccount("RO565656")
                .amount(BigDecimal.valueOf(445))
                .transactionDate(LocalDateTime.parse("2020-01-02T09:10:10"))
                .build();

        Transaction transactionResult = Transaction.builder()
                .accountId(1)
                .destinationAccount("RO565656")
                .amount(BigDecimal.valueOf(445))
                .transactionDate(LocalDateTime.parse("2020-01-02T09:10:10"))
                .build();
        when(accountService.getAccountById(transaction1.getAccountId())).thenReturn(account1);
        when(accountService.getAccountByAccountNumber(transaction1.getDestinationAccount())).thenReturn(accountByNumber);
        when(accountService.getAccounts()).thenReturn(Arrays.asList(accountByNumber));
        when(transactionRepository.save(transaction1)).thenReturn(transactionResult);

        Transaction actualTransaction = transactionService.createTransaction(transaction1);

        verify(accountService, times(2)).updateAccountBalance(any());

        assertThat(actualTransaction).isEqualToComparingFieldByField(transactionResult);
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

        when(accountService.getAccountById(transaction1.getAccountId())).thenReturn(account1);

        transactionService.createTransaction(transaction1);

        verify(transactionRepository).save(eq(transaction1));
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

        when(transactionRepository.findById(transaction1.getTransactionId())).
                thenReturn(transaction1).
                thenThrow(TransactionNotFoundException.class);

        transactionService.deleteTransactionById(transaction1.getTransactionId());

        verify(transactionRepository).deleteById(eq(transaction1.getTransactionId()));
    }
}
