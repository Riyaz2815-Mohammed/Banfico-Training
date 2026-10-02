package com.riyaz.banficotrainingprogram.payment.service.impl;

import com.riyaz.banficotrainingprogram.customer.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.payment.entity.Payment;
import com.riyaz.banficotrainingprogram.payment.repository.PaymentRepo;
import com.riyaz.banficotrainingprogram.payment.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepo paymentRepo;
    private final CustomerRepo customerRepo;

    public PaymentServiceImpl(PaymentRepo paymentRepo, CustomerRepo customerRepo) {
        this.paymentRepo = paymentRepo;
        this.customerRepo = customerRepo;
    }

    private PaymentResponse toResponse(Payment p) {
        String name = p.getInitiatedBy().getFirstName() + " " + p.getInitiatedBy().getLastName();
        return new PaymentResponse(p.getPaymentId(), p.getFromAccount().getAccountNo(), p.getToAccountNo(), p.getToAccountName(), p.getAmount(), p.getNote(), p.getStatus(), name, p.getInitiatedAt(), p.getCompletedAt(), p.getFailureReason());
    }

    @Override
    public Page<PaymentResponse> getAllPayments(Pageable pageable) {
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by("initiatedAt").descending()));
        return paymentRepo.findAll(sorted).map(this::toResponse);
    }

    @Override
    public Page<PaymentResponse> getPaymentsByAccount(UUID accountId, Pageable pageable) {
        return paymentRepo.findByFromAccount_Id(accountId, pageable).map(this::toResponse);
    }

    @Override
    public Page<PaymentResponse> getMyPayments(String email, Pageable pageable) {
        var customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        return paymentRepo.findByInitiatedBy_Id(customer.getId(), pageable).map(this::toResponse);
    }

    @Override
    public PaymentResponse getPayment(UUID paymentId) {
        return toResponse(paymentRepo.findById(paymentId).orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId)));
    }
}
