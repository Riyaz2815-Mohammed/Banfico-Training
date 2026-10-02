package com.riyaz.banficotrainingprogram.transaction.controller;

import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferPreviewResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.transaction.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<TransferPreviewResponse> preview(@AuthenticationPrincipal Jwt jwt, @RequestParam UUID fromAccountId, @RequestParam String recipientAccountNo, @RequestParam Integer amount) {
        return ResponseEntity.ok(transferService.preview(jwt.getClaimAsString("email"), fromAccountId, recipientAccountNo, amount));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> transfer(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.transfer(jwt.getClaimAsString("email"), request));
    }
}
