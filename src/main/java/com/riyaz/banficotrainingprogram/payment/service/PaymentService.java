package com.riyaz.banficotrainingprogram.payment.service;

import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {
    Page<PaymentResponse> getPayments(String email, UUID accountId, boolean isStaff, Pageable pageable);
    PaymentResponse getPayment(UUID paymentId);
}
