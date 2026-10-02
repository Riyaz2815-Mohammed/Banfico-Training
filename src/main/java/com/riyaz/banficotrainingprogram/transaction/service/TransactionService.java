package com.riyaz.banficotrainingprogram.transaction.service;

import com.riyaz.banficotrainingprogram.transaction.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {
    Page<TransactionResponse> getTransactions(UUID accountId, String email, boolean isStaff, Pageable pageable);
}
