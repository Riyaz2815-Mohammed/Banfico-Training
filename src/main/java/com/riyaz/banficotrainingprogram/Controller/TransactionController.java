package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Service.TransactionService;
import com.riyaz.banficotrainingprogram.dto.TransactionRequest;
import com.riyaz.banficotrainingprogram.dto.TransactionResponse;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.repository.TransactionsRepo;
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
    private final TransactionsRepo transactionsRepo;
    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;

    public TransactionController(TransactionService transactionService, TransactionsRepo transactionsRepo, AccountRepo accountRepo, CustomerRepo customerRepo) {
        this.transactionService = transactionService;
        this.transactionsRepo = transactionsRepo;
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(@RequestParam UUID accountId, @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        if (!isStaff) {
            Customer customer = customerRepo.findByEmail(jwt.getClaimAsString("email")).orElseThrow(() -> new ResourceNotFoundException("No customer record found for this account"));
            Account account = accountRepo.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
            if (!account.getCustomer().getId().equals(customer.getId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<TransactionResponse> responses = transactionsRepo.findByAccountIdOrderByTransactionTimeDesc(accountId).stream().map(t -> new TransactionResponse(
                t.getId(), t.getType(), t.getAccount().getId(), t.getBalanceAfter(), t.getAmount(), t.getTransactionTime(), t.getDescription()
        )).toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request.getAccountId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
