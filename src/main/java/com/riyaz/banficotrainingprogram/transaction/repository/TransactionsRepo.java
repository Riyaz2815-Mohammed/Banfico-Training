package com.riyaz.banficotrainingprogram.transaction.repository;

import com.riyaz.banficotrainingprogram.transaction.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionsRepo extends JpaRepository<Transactions, UUID> {
    Page<Transactions> findByAccountId(UUID accountId, Pageable pageable);
}
