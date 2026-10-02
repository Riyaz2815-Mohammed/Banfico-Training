package com.riyaz.banficotrainingprogram.payment.repository;

import com.riyaz.banficotrainingprogram.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    Page<Payment> findByFromAccount_Id(UUID accountId, Pageable pageable);
    Page<Payment> findByInitiatedBy_Id(UUID customerId, Pageable pageable);
}
