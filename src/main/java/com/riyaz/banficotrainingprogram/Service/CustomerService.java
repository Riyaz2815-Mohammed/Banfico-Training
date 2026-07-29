package com.riyaz.banficotrainingprogram.Service;

import com.riyaz.banficotrainingprogram.dto.CustomerRequest;
import com.riyaz.banficotrainingprogram.dto.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(UUID id);
    CustomerResponse updateCustomer(UUID id, CustomerRequest request);
    void deleteCustomer(UUID id);
}
