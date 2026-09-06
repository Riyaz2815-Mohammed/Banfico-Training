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

    // ─── Accounts ────────────────────────────────────────────────────────────

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        List<AccountResponse> accountResponses = accountRepo.findByCustomerId(customer.getId())
                .stream()
                .map(account -> new AccountResponse(
                        account.getId(), account.getAccountNo(), account.getAccountType(),
                        account.getBalance(), account.getCustomer().getId(),
                        account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName()))
                .toList();
        return ResponseEntity.ok(accountResponses);
    }

    // ─── Beneficiaries ───────────────────────────────────────────────────────

    @GetMapping("/beneficiaries")
    public ResponseEntity<List<BeneficiaryResponse>> getMyBeneficiaries(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        List<Beneficiary> beneficiaries = beneficiaryRepo.findByCustomerId(customer.getId());
        List<BeneficiaryResponse> beneficiaryResponses = beneficiaries.stream().map(beneficiary -> {
            String accountHolderName = beneficiary.getBeneficiaryAccount().getCustomer().getFirstName() + " " + beneficiary.getBeneficiaryAccount().getCustomer().getLastName();
            return new BeneficiaryResponse(
                    beneficiary.getId(), beneficiary.getCustomer().getId(),
                    beneficiary.getBeneficiaryAccount().getId(), beneficiary.getBeneficiaryAccount().getAccountNo(),
                    beneficiary.getBeneficiaryAccount().getAccountType(), accountHolderName, beneficiary.getNickname());
        }).toList();
        return ResponseEntity.ok(beneficiaryResponses);
    }

    @PostMapping("/beneficiaries")
    public ResponseEntity<BeneficiaryResponse> addMyBeneficiary(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody BeneficiaryRequest beneficiaryRequest) {
        String email = jwt.getClaimAsString("email");
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        Account account = accountRepo.findById(beneficiaryRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + beneficiaryRequest.getAccountId()));
        Beneficiary beneficiary = new Beneficiary(customer, account, beneficiaryRequest.getNickname());
        Beneficiary savedBeneficiary = beneficiaryRepo.save(beneficiary);
        String accountHolderName = savedBeneficiary.getBeneficiaryAccount().getCustomer().getFirstName() + " " + savedBeneficiary.getBeneficiaryAccount().getCustomer().getLastName();
        BeneficiaryResponse beneficiaryResponse = new BeneficiaryResponse(
                savedBeneficiary.getId(), savedBeneficiary.getCustomer().getId(),
                savedBeneficiary.getBeneficiaryAccount().getId(), savedBeneficiary.getBeneficiaryAccount().getAccountNo(),
                savedBeneficiary.getBeneficiaryAccount().getAccountType(), accountHolderName, savedBeneficiary.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryResponse);
    }

    @DeleteMapping("/beneficiaries/{beneficiaryId}")
    public ResponseEntity<Void> removeMyBeneficiary(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID beneficiaryId) {
        String email = jwt.getClaimAsString("email");
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + beneficiaryId));
        if (!beneficiary.getCustomer().getId().equals(customer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        beneficiaryRepo.deleteById(beneficiaryId);
        return ResponseEntity.noContent().build();
    }
}
