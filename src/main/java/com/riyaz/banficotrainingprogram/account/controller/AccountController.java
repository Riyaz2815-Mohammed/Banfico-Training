package com.riyaz.banficotrainingprogram.account.controller;

import com.riyaz.banficotrainingprogram.account.dto.AccountLookupResponse;
import com.riyaz.banficotrainingprogram.account.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.account.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.account.service.AccountService;
import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
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
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccounts(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        return ResponseEntity.ok(ApiResponse.ok("Accounts retrieved", accountService.getAccounts(jwt.getClaimAsString("email"), roles.contains("admin") || roles.contains("BankManager"))));
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
