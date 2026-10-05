package com.riyaz.banficotrainingprogram.customer.controller;

import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import com.riyaz.banficotrainingprogram.customer.dto.CustomerResponse;
import com.riyaz.banficotrainingprogram.customer.dto.ProfileUpdateRequest;
import com.riyaz.banficotrainingprogram.customer.dto.ProfileUpdateResponse;
import com.riyaz.banficotrainingprogram.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final CustomerService customerService;

    public ProfileController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> getProfile(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Profile retrieved", customerService.getMyProfile((String) auth.getPrincipal())));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileUpdateResponse>> updateProfile(@RequestBody ProfileUpdateRequest request, Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", customerService.updateMyProfile((String) auth.getPrincipal(), request)));
    }
}
