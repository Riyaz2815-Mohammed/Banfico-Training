package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.dto.AccountResponse;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    public MeController(CustomerRepo customerRepo, AccountRepo accountRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        return customerRepo.findByEmail(email)
                .map(customer -> {
                    List<AccountResponse> responses = accountRepo.findByCustomerId(customer.getId())
                            .stream()
                            .map(a -> new AccountResponse(
                                    a.getId(),
                                    a.getAccountNo(),
                                    a.getAccountType(),
                                    a.getBalance(),
                                    a.getCustomer().getId()))
                            .toList();
                    return ResponseEntity.ok(responses);
                })
                .orElseGet(() -> ResponseEntity.ok(List.of()));
    }
}
