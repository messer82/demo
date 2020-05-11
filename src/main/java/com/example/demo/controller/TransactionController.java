package com.example.demo.controller;

import com.example.demo.domain.entity.Transaction;
import com.example.demo.domain.model.TransactionPatch;
import com.example.demo.exception.AmountTooLargeException;
import com.example.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactionsByAccountId(@PathVariable(name = "account_id") int accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    @GetMapping("/transaction_id/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction getTransactionById(@PathVariable(name = "transaction_id") int transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction makeTransaction(@RequestBody @Valid Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @DeleteMapping("/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTransactionById(@PathVariable(name = "transaction_id") int transactionId) {
        transactionService.deleteTransactionById(transactionId);
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
