package com.riyaz.banficotrainingprogram.customer.controller;

import com.riyaz.banficotrainingprogram.customer.dto.CreateManagerRequest;
import com.riyaz.banficotrainingprogram.customer.dto.CreateManagerResponse;
import com.riyaz.banficotrainingprogram.customer.dto.ManagerResponse;
import com.riyaz.banficotrainingprogram.customer.service.KeycloakAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/managers")
public class ManagerController {
    private final KeycloakAdminService keycloakAdminService;

    public ManagerController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getManagers() {
        return ResponseEntity.ok(keycloakAdminService.getManagers());
    }

    @PostMapping
    public ResponseEntity<CreateManagerResponse> createManager(@Valid @RequestBody CreateManagerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(keycloakAdminService.createManager(request.getUsername(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getTemporaryPassword()));
    }
}
