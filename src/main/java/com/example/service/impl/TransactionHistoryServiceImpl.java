package com.example.service.impl;

import com.example.model.TransactionHistory;
import com.example.repository.TransactionHistoryRepository;
import com.example.service.TransactionHistoryService;
import jakarta.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    public TransactionHistoryServiceImpl(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }
    @Override
    public List<TransactionHistory> findAll() {
        return transactionHistoryRepository.findAll();
    }

    @Override
    public TransactionHistory save(TransactionHistory transactionHistory) {
        return transactionHistoryRepository.save(transactionHistory);
    }
}
