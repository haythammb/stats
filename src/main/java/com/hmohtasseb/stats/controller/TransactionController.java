package com.hmohtasseb.stats.controller;

import com.hmohtasseb.stats.model.Transaction;
import com.hmohtasseb.stats.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.hmohtasseb.stats.service.TransactionService.TIMEOUT_SEC;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @RequestMapping(value = "/transactions", method = POST)
    public ResponseEntity<Void> recordTransaction(@Valid @NotNull @RequestBody Transaction transaction) {
        if (transaction.getDurationInSec() > TIMEOUT_SEC) {
            // do not record it and return 204
            return new ResponseEntity<>(NO_CONTENT);
        }

        transactionService.recordTransaction(transaction);
        return new ResponseEntity<>(CREATED);
    }
}
