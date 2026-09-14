package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.TransferService;
import com.riyaz.banficotrainingprogram.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.dto.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.transfer(jwt.getClaimAsString("email"), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
