package com.example.service;

import com.example.model.TransactionHistory;
import jakarta.transaction.Transaction;

import java.util.List;

public interface TransactionHistoryService {
    List<TransactionHistory> findAll();
    TransactionHistory save(TransactionHistory transactionHistory);
}
