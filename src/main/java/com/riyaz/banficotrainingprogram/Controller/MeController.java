package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Beneficiary;
import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryResponse;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.BeneficiaryRepo;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final BeneficiaryRepo beneficiaryRepo;

    public MeController(CustomerRepo customerRepo, AccountRepo accountRepo, BeneficiaryRepo beneficiaryRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.beneficiaryRepo = beneficiaryRepo;
    }

    private Customer resolveCustomer(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        return customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
    }

    private BeneficiaryResponse toResponse(Beneficiary b) {
        Account acc = b.getBeneficiaryAccount();
        Customer holder = acc.getCustomer();
        return new BeneficiaryResponse(
                b.getId(),
                b.getCustomer().getId(),
                acc.getId(),
                acc.getAccountNo(),
                acc.getAccountType(),
                holder.getFirstName() + " " + holder.getLastName(),
                b.getNickname()
        );
    }

    // ─── Accounts ────────────────────────────────────────────────────────────

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts(@AuthenticationPrincipal Jwt jwt) {
        Customer customer = resolveCustomer(jwt);
        List<AccountResponse> responses = accountRepo.findByCustomerId(customer.getId())
                .stream()
                .map(a -> new AccountResponse(
                        a.getId(), a.getAccountNo(), a.getAccountType(),
                        a.getBalance(), a.getCustomer().getId()))
                .toList();
        return ResponseEntity.ok(responses);
    }

    // ─── Beneficiaries ───────────────────────────────────────────────────────

    @GetMapping("/beneficiaries")
    public ResponseEntity<List<BeneficiaryResponse>> getMyBeneficiaries(@AuthenticationPrincipal Jwt jwt) {
        Customer customer = resolveCustomer(jwt);
        List<BeneficiaryResponse> responses = beneficiaryRepo.findByCustomerId(customer.getId())
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/beneficiaries")
    public ResponseEntity<BeneficiaryResponse> addMyBeneficiary(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody BeneficiaryRequest request) {
        Customer customer = resolveCustomer(jwt);
        Account account = accountRepo.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));
        Beneficiary beneficiary = new Beneficiary(customer, account, request.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(beneficiaryRepo.save(beneficiary)));
    }

    @DeleteMapping("/beneficiaries/{beneficiaryId}")
    public ResponseEntity<Void> removeMyBeneficiary(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID beneficiaryId) {
        Customer customer = resolveCustomer(jwt);
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
        if (!beneficiary.getCustomer().getId().equals(customer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        beneficiaryRepo.deleteById(beneficiaryId);
        return ResponseEntity.noContent().build();
    }
}
