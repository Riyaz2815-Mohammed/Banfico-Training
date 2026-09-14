package com.riyaz.banficotrainingprogram.Service;

import com.riyaz.banficotrainingprogram.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.dto.TransferResponse;

public interface TransferService {
    TransferResponse transfer(String email, TransferRequest request);
}
