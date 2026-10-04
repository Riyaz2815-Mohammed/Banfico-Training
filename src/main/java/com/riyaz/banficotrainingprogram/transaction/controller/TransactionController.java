package com.riyaz.banficotrainingprogram.transaction.controller;

import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import com.riyaz.banficotrainingprogram.transaction.dto.TransactionResponse;
import com.riyaz.banficotrainingprogram.transaction.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getTransactions(@RequestParam UUID accountId, Authentication auth, @PageableDefault(size = 20, sort = "transactionTime", direction = Sort.Direction.DESC) Pageable pageable) {
        String email = (String) auth.getPrincipal();
        boolean isStaff = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_BANKMANAGER"));
        Page<TransactionResponse> result = transactionService.getTransactions(accountId, email, isStaff, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(ApiResponse.ok("Transactions retrieved", result));
    }
}
