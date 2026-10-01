package com.riyaz.banficotrainingprogram.transaction.service;

import com.riyaz.banficotrainingprogram.transaction.dto.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionResponse> getTransactions(UUID accountId, String email, boolean isStaff);
}
