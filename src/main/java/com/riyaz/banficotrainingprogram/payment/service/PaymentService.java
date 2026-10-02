package com.riyaz.banficotrainingprogram.payment.service;

import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {
    Page<PaymentResponse> getAllPayments(Pageable pageable);
    Page<PaymentResponse> getPaymentsByAccount(UUID accountId, Pageable pageable);
    Page<PaymentResponse> getMyPayments(String email, Pageable pageable);
    PaymentResponse getPayment(UUID paymentId);
}
