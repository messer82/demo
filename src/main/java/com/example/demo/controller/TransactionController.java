package com.example.demo.controller;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.AccountPatch;
import com.example.demo.domain.model.TransactionPatch;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.AmountTooLargeException;
import com.example.demo.exception.TransactionNotFoundException;
import com.example.demo.service.AccountService;
import com.example.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
//    private final AccountService accountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactionsByAccountId(@PathVariable(name = "account_id") int account_id) {
        try {
            return transactionService.getTransactionsByAccount(account_id);
        } catch (EmptyResultDataAccessException exception) {
            throw new AccountNotFoundException("No transactions for this account!");
        }
    }

    @GetMapping("/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction getTransactionById(@PathVariable(name = "transaction_id") int transaction_id) {
        try {
            return transactionService.getTransactionById(transaction_id);
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage(), exception);
            throw new TransactionNotFoundException("No transaction for this id!");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction makeTransaction(@RequestBody @Valid Transaction transaction) {
        try {
            return transactionService.createTransaction(transaction);
        } catch (DataIntegrityViolationException exception) {
            throw new AccountNotFoundException("No valid account from which to make the payment!");
        }
    }

    @PatchMapping("/{transaction_id")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction makeRecurrentPayment(@PathVariable int transaction_id, @RequestBody TransactionPatch transactionPatch) {
        try {
            transactionPatch.setTransaction_id(transaction_id);
            return transactionService.updatePartialTransaction(transactionPatch);
        } catch (EmptyResultDataAccessException exception) {
            throw new TransactionNotFoundException("No previous transactions for this id!");
        }
    }

    @DeleteMapping("/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTransactionById(@PathVariable(name = "transaction_id") int transaction_id) {
        if (getTransactionById(transaction_id).getTransaction_id() > 0) {
            transactionService.deleteTransactionById(transaction_id);
        } else {
            throw new TransactionNotFoundException();
        }
    }

    @ExceptionHandler(AmountTooLargeException.class)
    public void amountTooLarge(HttpServletResponse response) throws MethodArgumentNotValidException {
        try {
            response.sendError(HttpStatus.PAYLOAD_TOO_LARGE.value());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
