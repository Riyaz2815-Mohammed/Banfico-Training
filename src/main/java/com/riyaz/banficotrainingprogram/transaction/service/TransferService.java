package com.riyaz.banficotrainingprogram.transaction.service;

import com.riyaz.banficotrainingprogram.transaction.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferResponse;

public interface TransferService {
    TransferResponse transfer(String email, TransferRequest request);
}
