package com.riyaz.banficotrainingprogram.customer.controller;

import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ManagerResponse>>> getManagers() {
        return ResponseEntity.ok(ApiResponse.ok("Managers retrieved", keycloakAdminService.getManagers()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreateManagerResponse>> createManager(@Valid @RequestBody CreateManagerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("Manager created", keycloakAdminService.createManager(request.getUsername(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getTemporaryPassword())));
    }
}
