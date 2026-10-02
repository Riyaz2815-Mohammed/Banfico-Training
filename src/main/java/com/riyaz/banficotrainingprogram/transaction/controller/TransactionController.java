package com.riyaz.banficotrainingprogram.transaction.controller;

import com.riyaz.banficotrainingprogram.transaction.dto.TransactionResponse;
import com.riyaz.banficotrainingprogram.transaction.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(@RequestParam UUID accountId, @AuthenticationPrincipal Jwt jwt, @PageableDefault(size = 20, sort = "transactionTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        Page<TransactionResponse> result = transactionService.getTransactions(accountId, jwt.getClaimAsString("email"), isStaff, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(result);
    }
}
