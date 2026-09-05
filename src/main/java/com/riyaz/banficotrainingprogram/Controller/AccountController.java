package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.AccountService;
import com.riyaz.banficotrainingprogram.dto.AccountLookupResponse;
import com.riyaz.banficotrainingprogram.dto.AccountRequest;
import com.riyaz.banficotrainingprogram.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountRepo accountRepo;

    public AccountController(AccountService accountService, AccountRepo accountRepo) {
        this.accountService = accountService;
        this.accountRepo = accountRepo;
    }

    @GetMapping("/lookup")
    public ResponseEntity<AccountLookupResponse> lookupByAccountNo(@RequestParam String accountNo) {
        return accountRepo.findByAccountNo(accountNo)
                .map(a -> {
                    String holderName = a.getCustomer().getFirstName() + " " + a.getCustomer().getLastName();
                    return ResponseEntity.ok(new AccountLookupResponse(a.getId(), a.getAccountNo(), a.getAccountType(), holderName));
                })
                .orElseThrow(() -> new ResourceNotFoundException("No account found with number: " + accountNo));
    }
    @PostMapping
    public ResponseEntity<AccountResponse> createaccount(@Valid @RequestBody AccountRequest account) {
        AccountResponse accountResponse = accountService.createAccount(account);
        return  ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accountResponses = accountService.getAccounts();
        return ResponseEntity.ok(accountResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID id) {
        AccountResponse accountResponse = accountService.getAccount(id);
        return ResponseEntity.ok(accountResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable UUID id ,@Valid @RequestBody AccountRequest account) {
        AccountResponse accountResponse= accountService.updateAccount(id, account);
        return ResponseEntity.ok(accountResponse);
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
        return "Account "+ " "+id+ "has been Deleted";
    }
}
