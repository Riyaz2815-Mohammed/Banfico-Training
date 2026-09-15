package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.TransactionService;
import com.riyaz.banficotrainingprogram.dto.TransactionRequest;
import com.riyaz.banficotrainingprogram.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam UUID accountId, @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        return ResponseEntity.ok(transactionService.getTransactions(accountId, jwt.getClaimAsString("email"), isStaff));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(request.getAccountId(), request));
    }
}
