package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Service.CustomerService;
import com.riyaz.banficotrainingprogram.dto.CustomerRequest;
import com.riyaz.banficotrainingprogram.dto.CustomerResponse;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepo customerRepo;
    public CustomerServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        Customer customer = new Customer(
                request.getPan(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber()
        );

        Customer savedCustomer = customerRepo.save(customer);

        return new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getPan(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedCustomer.getEmail(),
                savedCustomer.getPhoneNumber()
        );
    }
    @Override
    public List<CustomerResponse> getAllCustomers() {

        List<Customer> customers = customerRepo.findAll();

        return customers.stream().map(customer -> new CustomerResponse(
                        customer.getId(),
                        customer.getPan(),
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail(),
                        customer.getPhoneNumber()
                ))
                .toList();
    }


    @Override
    public CustomerResponse getCustomerById(UUID id) {

        Customer customer = customerRepo.findById(id).orElse(null);

        if (customer == null) {
            return null;
        }

        return new CustomerResponse(
                customer.getId(),
                customer.getPan(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }


    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {

        Customer customer = customerRepo.findById(id).orElse(null);

        if (customer == null) {
            return null;
        }

        customer.setPan(request.getPan());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        Customer updatedCustomer = customerRepo.save(customer);

        return new CustomerResponse(
                updatedCustomer.getId(),
                updatedCustomer.getPan(),
                updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getEmail(),
                updatedCustomer.getPhoneNumber()
        );
    }

    @Override
    public void deleteCustomer(UUID id) {

        customerRepo.deleteById(id);
    }

}
