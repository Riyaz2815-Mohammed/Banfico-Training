package com.riyaz.banficotrainingprogram.customer.service;

import com.riyaz.banficotrainingprogram.customer.dto.CustomerRequest;
import com.riyaz.banficotrainingprogram.customer.dto.CustomerResponse;
import com.riyaz.banficotrainingprogram.customer.dto.ProfileUpdateRequest;
import com.riyaz.banficotrainingprogram.customer.dto.ProfileUpdateResponse;
import com.riyaz.banficotrainingprogram.customer.dto.RegisterRequest;
import com.riyaz.banficotrainingprogram.customer.dto.RegisterResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(UUID id);
    CustomerResponse updateCustomer(UUID id, CustomerRequest request);
    void deleteCustomer(UUID id);
    RegisterResponse registerCustomerWithKeycloak(RegisterRequest request);
    CustomerResponse getMyProfile(String email);
    ProfileUpdateResponse updateMyProfile(String email, ProfileUpdateRequest request);
}
