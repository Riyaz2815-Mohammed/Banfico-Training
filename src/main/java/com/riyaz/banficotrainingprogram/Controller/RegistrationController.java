package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Service.KeycloakAdminService;
import com.riyaz.banficotrainingprogram.dto.RegisterRequest;
import com.riyaz.banficotrainingprogram.dto.RegisterResponse;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final CustomerRepo customerRepo;
    private final KeycloakAdminService keycloakAdminService;

    public RegistrationController(CustomerRepo customerRepo, KeycloakAdminService keycloakAdminService) {
        this.customerRepo = customerRepo;
        this.keycloakAdminService = keycloakAdminService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        String keycloakUserId = keycloakAdminService.createUser(
                request.getUsername(), request.getEmail(),
                request.getFirstName(), request.getLastName(),
                request.getTemporaryPassword()
        );

        Customer customer = new Customer(
                request.getPan(), request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPhoneNumber()
        );
        Customer saved = customerRepo.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(
                saved.getId(), saved.getFirstName(), saved.getLastName(),
                saved.getEmail(), saved.getPan(), saved.getPhoneNumber(),
                keycloakUserId
        ));
    }
}
