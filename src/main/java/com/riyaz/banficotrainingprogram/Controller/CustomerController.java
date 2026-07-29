package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.CustomerService;
import com.riyaz.banficotrainingprogram.dto.CustomerRequest;
import com.riyaz.banficotrainingprogram.dto.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerResponse);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        List<CustomerResponse> customerResponseList = customerService.getAllCustomers();
        return ResponseEntity.ok(customerResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer" + id +"  has been deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id, @RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(customerResponse);
    }

}
