package com.riyaz.banficotrainingprogram.account.controller;

import com.riyaz.banficotrainingprogram.account.dto.AccountLookupResponse;
import com.riyaz.banficotrainingprogram.account.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.account.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.account.service.AccountService;
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
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        if (isStaff) return ResponseEntity.ok(accountService.getAccounts());
        return ResponseEntity.ok(accountService.getMyAccounts(jwt.getClaimAsString("email")));
    }

    @GetMapping("/lookup")
    public ResponseEntity<AccountLookupResponse> lookupByAccountNo(@RequestParam String accountNo) {
        return ResponseEntity.ok(accountService.lookupByAccountNo(accountNo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable UUID id, @Valid @RequestBody AccountRequest account) {
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
