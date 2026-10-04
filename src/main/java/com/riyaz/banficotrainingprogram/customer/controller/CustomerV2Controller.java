package com.riyaz.banficotrainingprogram.customer.controller;

import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import com.riyaz.banficotrainingprogram.customer.dto.RegisterRequest;
import com.riyaz.banficotrainingprogram.customer.dto.RegisterResponse;
import com.riyaz.banficotrainingprogram.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/customers")
public class CustomerV2Controller {
    private final CustomerService customerService;

    public CustomerV2Controller(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RegisterResponse>> createCustomer(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("Customer registered", customerService.registerCustomerWithKeycloak(request)));
    }
}
