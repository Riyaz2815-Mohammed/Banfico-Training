package com.riyaz.banficotrainingprogram.transaction.service;

import com.riyaz.banficotrainingprogram.transaction.dto.TransactionRequest;
import com.riyaz.banficotrainingprogram.transaction.dto.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse createTransaction(UUID accountId, TransactionRequest request);
    List<TransactionResponse> getTransactions(UUID accountId, String email, boolean isStaff);
}
