package com.riyaz.banficotrainingprogram.transaction.repository;

import com.riyaz.banficotrainingprogram.transaction.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionsRepo extends JpaRepository<Transactions, UUID> {
    List<Transactions> findByAccountIdOrderByTransactionTimeDesc(UUID accountId);
}
