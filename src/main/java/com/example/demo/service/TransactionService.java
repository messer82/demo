package com.example.demo.service;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.domain.model.TransactionPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.AmountTooLargeException;
import com.example.demo.exception.BalanceTooLowException;
import com.example.demo.repository.AccountRepository;
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
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(int accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.findById(transactionId);
    }

    public Transaction createTransaction(Transaction transaction) {
        BigDecimal payerInitialBalance = accountService.getAccountById(transaction.getAccount_id()).getBalance();
        if (transaction.getAmount().compareTo(BigDecimal.valueOf(2000)) > 0) {
            throw new AmountTooLargeException("Transaction limit is of 2000!");
        }
        if (payerInitialBalance.compareTo(transaction.getAmount()) > 0) {

                if (accountService.getAccounts().contains(accountService.getAccountByAccountNumber(transaction.getDestination_account()))) {
                    accountService.updateAccountBalance(AccountPatch.builder().
                            accountId(transaction.getAccount_id()).
                            balance(payerInitialBalance.subtract(transaction.getAmount())).build());

                    BigDecimal receiverInitialBalance = accountService.getAccountByAccountNumber(transaction.getDestination_account()).getBalance();

                    accountService.updateAccountBalance(AccountPatch.builder().
                            accountId(accountService.getAccountByAccountNumber(transaction.getDestination_account()).getAccount_id()).
                            balance(receiverInitialBalance.add(transaction.getAmount())).build());
                } else {
                    accountService.updateAccountBalance(AccountPatch.builder().
                            accountId(transaction.getAccount_id()).
                            balance(payerInitialBalance.subtract(transaction.getAmount())).build());
                }

            return transactionRepository.save(transaction);
        } else {
            throw new BalanceTooLowException("Insufficient funds!");
        }
    }

    public void deleteTransactionById(int id) {
        transactionRepository.deleteById(id);
    }

    //for recurrent payments to the same account (e.g. utility bill payment)
    public Transaction updatePartialTransaction(@Valid TransactionPatch transactionPatch) {
        Transaction transaction = transactionRepository.findById(transactionPatch.getTransaction_id());
        transaction.setAmount(transactionPatch.getAmount());
        return transactionRepository.save(transaction);
    }
}
