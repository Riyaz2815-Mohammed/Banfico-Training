package com.riyaz.banficotrainingprogram.transaction.service;

import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferRequest;

public interface TransferService {
    PaymentResponse transfer(String email, TransferRequest request);
}
