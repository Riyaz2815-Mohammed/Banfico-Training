package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Beneficiary;
import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Entity.Transactions;
import com.riyaz.banficotrainingprogram.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryResponse;
import com.riyaz.banficotrainingprogram.dto.TransferRequest;
import com.riyaz.banficotrainingprogram.dto.TransferResponse;
import com.riyaz.banficotrainingprogram.exception.InsufficientBalanceException;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.BeneficiaryRepo;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.repository.TransactionsRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final BeneficiaryRepo beneficiaryRepo;
    private final TransactionsRepo transactionsRepo;

    public MeController(CustomerRepo customerRepo, AccountRepo accountRepo, BeneficiaryRepo beneficiaryRepo, TransactionsRepo transactionsRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.beneficiaryRepo = beneficiaryRepo;
        this.transactionsRepo = transactionsRepo;
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

    // ─── Transfer ─────────────────────────────────────────────────────────────

    @PostMapping("/transfer")
    @Transactional
    public ResponseEntity<TransferResponse> transfer(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TransferRequest transferRequest) {
        String email = jwt.getClaimAsString("email");
        Customer senderCustomer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));

        Account fromAccount = accountRepo.findById(transferRequest.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + transferRequest.getFromAccountId()));
        if (!fromAccount.getCustomer().getId().equals(senderCustomer.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Account toAccount = accountRepo.findByAccountNo(transferRequest.getRecipientAccountNo())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with number: " + transferRequest.getRecipientAccountNo()));
        if (fromAccount.getId().equals(toAccount.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (fromAccount.getBalance() < transferRequest.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance: available " + fromAccount.getBalance() + ", requested " + transferRequest.getAmount());
        }

        fromAccount.setBalance(fromAccount.getBalance() - transferRequest.getAmount());
        accountRepo.save(fromAccount);
        toAccount.setBalance(toAccount.getBalance() + transferRequest.getAmount());
        accountRepo.save(toAccount);

        String senderName = senderCustomer.getFirstName() + " " + senderCustomer.getLastName();
        String recipientName = toAccount.getCustomer().getFirstName() + " " + toAccount.getCustomer().getLastName();
        String debitDescription = "Transfer to " + recipientName + " (" + toAccount.getAccountNo() + ")";
        String creditDescription = "Transfer from " + senderName + " (" + fromAccount.getAccountNo() + ")";

        LocalDateTime now = LocalDateTime.now();
        Transactions debitTransaction = transactionsRepo.save(new Transactions("DEBIT", transferRequest.getAmount(), now, fromAccount, debitDescription, fromAccount.getBalance()));
        transactionsRepo.save(new Transactions("CREDIT", transferRequest.getAmount(), now, toAccount, creditDescription, toAccount.getBalance()));

        String note = transferRequest.getNote() != null ? transferRequest.getNote() : "";
        TransferResponse transferResponse = new TransferResponse(
                debitTransaction.getId(), fromAccount.getAccountNo(), fromAccount.getBalance(),
                recipientName, toAccount.getAccountNo(), transferRequest.getAmount(), note, now);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferResponse);
    }
}
