package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.dto.TransactionDto;
import com.ravemaster.inventory.domain.dto.TransactionDtoSecond;
import com.ravemaster.inventory.domain.request.TransactionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {
    TransactionDto createTransaction(TransactionRequest request);
    Page<TransactionDto> getTransactions(Pageable pageable);
    Page<TransactionDto> getTransactionsByType(Pageable pageable, String transactionType);
    Page<TransactionDto> getTransactionsBySale(Pageable pageable, String saleType);
    TransactionDtoSecond getTransactionWithLines(UUID id);
    void deleteTransaction(UUID id);
}
