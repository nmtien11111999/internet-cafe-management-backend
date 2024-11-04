package com.example.controller;

import com.example.model.TransactionHistory;
import com.example.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/history")
public class TransactionHistoryController {
    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @GetMapping
    public ResponseEntity<?> getHistory(){
        return new ResponseEntity<>(transactionHistoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addHistory(@RequestBody TransactionHistory transactionHistory){
        return new ResponseEntity<>(transactionHistoryService.save(transactionHistory), HttpStatus.CREATED);
    }
}
