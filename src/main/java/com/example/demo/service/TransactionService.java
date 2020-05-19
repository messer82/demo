package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.AmountTooLargeException;
import com.example.demo.exception.BalanceTooLowException;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public List<Transaction> getAllTransactions() {
        try {
            return transactionRepository.findAll();
        } catch (EmptyResultDataAccessException exception) {
            throw new TransactionNotFoundException("No transactions available!");
        }
    }

    public List<Transaction> getTransactionsByAccount(int accountId) {
        try {
            return transactionRepository.findByAccountId(accountId);
        } catch (EmptyResultDataAccessException exception) {
            throw new TransactionNotFoundException("No transactions found for this account id!");
        }
    }

    public Transaction getTransactionById(int transactionId) {
        try {
            return transactionRepository.findById(transactionId);
        } catch (EmptyResultDataAccessException exception) {
            throw new TransactionNotFoundException("No transactions found for this transaction id!");
        }
    }

    public Transaction createTransaction(Transaction transaction) {
        BigDecimal payerInitialBalance = accountService.getAccountById(transaction.getAccountId()).getBalance();
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(2000)) > 0) {
            throw new AmountTooLargeException("Transaction limit is of 2000!");
        }
        if (payerInitialBalance.compareTo(transaction.getAmount()) > 0) {
//            payment / transfer between accounts belonging to the same bank

            Account accountByAccountNumber = null;
                    try {
                        accountByAccountNumber = accountService.getAccountByAccountNumber(transaction.getDestinationAccount());
                    } catch (AccountNotFoundException exception) {
                        log.error("Exception getting destination account by account number!", exception);
                    }
            if (accountService.getAccounts().contains(accountByAccountNumber)) {
                accountService.updateAccountBalance(AccountPatch.builder().
                        accountId(transaction.getAccountId()).
                        balance(payerInitialBalance.subtract(transaction.getAmount())).build());

                BigDecimal receiverInitialBalance = accountByAccountNumber.getBalance();

                accountService.updateAccountBalance(AccountPatch.builder().
                        accountId(accountByAccountNumber.getAccountId()).
                        balance(receiverInitialBalance.add(transaction.getAmount())).build());
            } else {
//                payment to an external bank account

                accountService.updateAccountBalance(AccountPatch.builder().
                        accountId(transaction.getAccountId()).
                        balance(payerInitialBalance.subtract(transaction.getAmount())).build());
            }

            return transactionRepository.save(transaction);
        } else {
            throw new BalanceTooLowException("Insufficient funds!");
        }
    }

    public void deleteTransactionById(int id) {
//        check that transaction exists, else throw TransactionNotFoundException
        Transaction transaction = getTransactionById(id);

        transactionRepository.deleteById(id);
    }
}
