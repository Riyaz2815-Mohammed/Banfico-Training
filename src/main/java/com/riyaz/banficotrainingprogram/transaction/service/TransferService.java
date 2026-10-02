package com.riyaz.banficotrainingprogram.transaction.service;

import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferPreviewResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferRequest;

import java.util.UUID;

public interface TransferService {
    TransferPreviewResponse preview(String email, UUID fromAccountId, String recipientAccountNo, Integer amount);
    PaymentResponse transfer(String email, TransferRequest request);
}
