package com.riyaz.banficotrainingprogram.customer.controller;

import com.riyaz.banficotrainingprogram.customer.dto.RegisterRequest;
import com.riyaz.banficotrainingprogram.customer.dto.RegisterResponse;
import com.riyaz.banficotrainingprogram.customer.service.CustomerService;
import com.riyaz.banficotrainingprogram.customer.service.KeycloakAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RegistrationController {
    private final CustomerService customerService;
    private final KeycloakAdminService keycloakAdminService;

    public RegistrationController(CustomerService customerService, KeycloakAdminService keycloakAdminService) {
        this.customerService = customerService;
        this.keycloakAdminService = keycloakAdminService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        String keycloakUserId = keycloakAdminService.createUser(request.getUsername(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getTemporaryPassword());
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(request, keycloakUserId));
        } catch (Exception dbEx) {
            try { keycloakAdminService.deleteUser(keycloakUserId); } catch (Exception ignored) {}
            throw dbEx;
        }
    }
}
