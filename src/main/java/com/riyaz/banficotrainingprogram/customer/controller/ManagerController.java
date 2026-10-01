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
import java.util.Map;

@RestController
@RequestMapping("/api/v1/managers")
public class ManagerController {
    private final KeycloakAdminService keycloakAdminService;

    public ManagerController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getManagers() {
        List<Map> users = keycloakAdminService.getManagers();
        List<ManagerResponse> managers = users.stream().map(u -> new ManagerResponse((String) u.get("id"), (String) u.get("username"), (String) u.get("email"), (String) u.get("firstName"), (String) u.get("lastName"))).toList();
        return ResponseEntity.ok(managers);
    }

    @PostMapping
    public ResponseEntity<CreateManagerResponse> createManager(@Valid @RequestBody CreateManagerRequest request) {
        String keycloakId = keycloakAdminService.createManager(request.getUsername(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getTemporaryPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateManagerResponse(keycloakId, request.getUsername(), request.getEmail(), request.getFirstName(), request.getLastName()));
    }
}
