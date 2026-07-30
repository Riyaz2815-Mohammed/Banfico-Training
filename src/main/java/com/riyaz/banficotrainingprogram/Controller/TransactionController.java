package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.TransactionService;
import com.riyaz.banficotrainingprogram.dto.TransactionRequest;
import com.riyaz.banficotrainingprogram.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts/{id}/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping
    public ResponseEntity<TransactionResponse> createtransaction(@PathVariable UUID id, @Valid @RequestBody TransactionRequest transaction) {
        TransactionResponse transactionResponse = transactionService.createTransaction(id, transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable UUID accountId) {
        List<TransactionResponse> transactionResponses = transactionService.getTransactions(accountId);
        return ResponseEntity.ok(transactionResponses);
    }
}
