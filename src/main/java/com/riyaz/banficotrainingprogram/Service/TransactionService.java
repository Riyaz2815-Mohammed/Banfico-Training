package com.riyaz.banficotrainingprogram.Service;

import com.riyaz.banficotrainingprogram.dto.TransactionRequest;
import com.riyaz.banficotrainingprogram.dto.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse createTransaction(UUID accountId, TransactionRequest request);
    List<TransactionResponse> getTransactions(UUID accountId, String email, boolean isStaff);
}
