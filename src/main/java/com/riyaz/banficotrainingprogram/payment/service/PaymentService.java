package com.riyaz.banficotrainingprogram.payment.service;

import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    List<PaymentResponse> getPaymentsByAccount(UUID accountId);
    List<PaymentResponse> getMyPayments(String email);
    PaymentResponse getPayment(UUID paymentId);
}
