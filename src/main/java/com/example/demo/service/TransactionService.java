package com.example.demo.service;

import com.example.demo.domain.entity.Account;
import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.domain.model.TransactionPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.AmountTooLargeException;
import com.example.demo.exception.BalanceTooLowException;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service

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
                        exception.printStackTrace();
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
        if (getTransactionById(id).getTransactionId() > 0) {
            transactionRepository.deleteById(id);
        } else {
            throw new TransactionNotFoundException();
        }
    }

    //for recurrent payments to the same account (e.g. utility bill payment)
    public Transaction updatePartialTransaction(@Valid TransactionPatch transactionPatch) {
        try {
            Transaction transaction = transactionRepository.findById(transactionPatch.getTransactionId());
            transaction.setAmount(transactionPatch.getAmount());
            return transactionRepository.save(transaction);
        } catch (EmptyResultDataAccessException exception) {
            throw new TransactionNotFoundException("No valid transaction details for patch!");
        }
    }
}
