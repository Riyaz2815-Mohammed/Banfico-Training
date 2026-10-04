package com.riyaz.banficotrainingprogram.transaction.controller;

import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferPreviewResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.transaction.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v2/transfer")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/preview")
    public ResponseEntity<ApiResponse<TransferPreviewResponse>> preview(Authentication auth, @RequestParam UUID fromAccountId, @RequestParam String recipientAccountNo, @RequestParam Integer amount) {
        return ResponseEntity.ok(ApiResponse.ok("Transfer preview", transferService.preview((String) auth.getPrincipal(), fromAccountId, recipientAccountNo, amount)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> transfer(Authentication auth, @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("Transfer successful", transferService.transfer((String) auth.getPrincipal(), request)));
    }
}
