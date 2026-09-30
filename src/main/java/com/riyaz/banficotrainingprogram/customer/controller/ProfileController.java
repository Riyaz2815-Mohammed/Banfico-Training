package com.riyaz.banficotrainingprogram.customer.controller;

import com.riyaz.banficotrainingprogram.customer.dto.CustomerRequest;
import com.riyaz.banficotrainingprogram.customer.dto.CustomerResponse;
import com.riyaz.banficotrainingprogram.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final CustomerService customerService;

    public ProfileController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> getProfile(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.getMyProfile(jwt.getClaimAsString("email")));
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> updateProfile(@RequestBody CustomerRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(customerService.updateMyProfile(jwt.getClaimAsString("email"), request));
    }
}
