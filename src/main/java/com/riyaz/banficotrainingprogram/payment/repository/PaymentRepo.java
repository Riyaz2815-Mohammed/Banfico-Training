package com.riyaz.banficotrainingprogram.payment.repository;

import com.riyaz.banficotrainingprogram.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    List<Payment> findByFromAccount_Id(UUID accountId);
    List<Payment> findByInitiatedBy_Id(UUID customerId);
    List<Payment> findAllByOrderByInitiatedAtDesc();
}
