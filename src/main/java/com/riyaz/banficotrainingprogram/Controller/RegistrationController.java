package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.CustomerService;
import com.riyaz.banficotrainingprogram.Service.KeycloakAdminService;
import com.riyaz.banficotrainingprogram.dto.RegisterRequest;
import com.riyaz.banficotrainingprogram.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
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
