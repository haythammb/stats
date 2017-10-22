package com.hmohtasseb.stats.controller;

import com.hmohtasseb.stats.model.InstantStats;
import com.hmohtasseb.stats.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class StatsController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/statistics")
    @ResponseBody
    public ResponseEntity<InstantStats> getTransactionStats() {
        return new ResponseEntity<>(transactionService.getInstantStats(), OK);
    }
}
