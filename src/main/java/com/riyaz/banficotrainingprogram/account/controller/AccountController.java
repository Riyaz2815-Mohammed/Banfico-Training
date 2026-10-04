package com.riyaz.banficotrainingprogram.account.controller;

import com.riyaz.banficotrainingprogram.account.dto.AccountLookupResponse;
import com.riyaz.banficotrainingprogram.account.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.account.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.account.service.AccountService;
import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccounts(Authentication auth) {
        String email = (String) auth.getPrincipal();
        boolean isStaff = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_BANKMANAGER"));
        return ResponseEntity.ok(ApiResponse.ok("Accounts retrieved", accountService.getAccounts(email, isStaff)));
    }

    @GetMapping("/lookup")
    public ResponseEntity<ApiResponse<AccountLookupResponse>> lookupByAccountNo(@RequestParam String accountNo) {
        return ResponseEntity.ok(ApiResponse.ok("Account found", accountService.lookupByAccountNo(accountNo)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok("Account retrieved", accountService.getAccount(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody AccountRequest account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("Account created", accountService.createAccount(account)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccount(@PathVariable UUID id, @Valid @RequestBody AccountRequest account) {
        return ResponseEntity.ok(ApiResponse.ok("Account updated", accountService.updateAccount(id, account)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.ok("Account deleted", null));
    }
}
