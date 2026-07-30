package com.riyaz.banficotrainingprogram.Service;

import com.riyaz.banficotrainingprogram.Entity.Transactions;
import com.riyaz.banficotrainingprogram.dto.TransactionRequest;
import com.riyaz.banficotrainingprogram.dto.TransactionResponse;
import jakarta.transaction.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse createTransaction(UUID id, TransactionRequest transaction);
    List<TransactionResponse> getTransactions(UUID id);
}
