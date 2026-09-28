package com.riyaz.banficotrainingprogram.customer.service.impl;

import com.riyaz.banficotrainingprogram.account.entity.Account;
import com.riyaz.banficotrainingprogram.account.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.beneficiary.repository.BeneficiaryRepo;
import com.riyaz.banficotrainingprogram.customer.dto.CustomerRequest;
import com.riyaz.banficotrainingprogram.customer.dto.CustomerResponse;
import com.riyaz.banficotrainingprogram.customer.dto.RegisterRequest;
import com.riyaz.banficotrainingprogram.customer.dto.RegisterResponse;
import com.riyaz.banficotrainingprogram.customer.entity.Customer;
import com.riyaz.banficotrainingprogram.customer.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.customer.service.CustomerService;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.transaction.repository.TransactionsRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;
    private final TransactionsRepo transactionsRepo;
    private final BeneficiaryRepo beneficiaryRepo;

    public CustomerServiceImpl(CustomerRepo customerRepo, AccountRepo accountRepo, TransactionsRepo transactionsRepo, BeneficiaryRepo beneficiaryRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.transactionsRepo = transactionsRepo;
        this.beneficiaryRepo = beneficiaryRepo;
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer savedCustomer = customerRepo.save(new Customer(request.getPan(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber()));
        return new CustomerResponse(savedCustomer.getId(), savedCustomer.getPan(), savedCustomer.getFirstName(), savedCustomer.getLastName(), savedCustomer.getEmail(), savedCustomer.getPhoneNumber());
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepo.findAll().stream().map(customer -> new CustomerResponse(customer.getId(), customer.getPan(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPhoneNumber())).toList();
    }

    @Override
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return new CustomerResponse(customer.getId(), customer.getPan(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPhoneNumber());
    }

    @Override
    public CustomerResponse updateCustomer(UUID id, CustomerRequest request) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setPan(request.getPan());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        Customer updatedCustomer = customerRepo.save(customer);
        return new CustomerResponse(updatedCustomer.getId(), updatedCustomer.getPan(), updatedCustomer.getFirstName(), updatedCustomer.getLastName(), updatedCustomer.getEmail(), updatedCustomer.getPhoneNumber());
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) {
        customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        beneficiaryRepo.deleteAll(beneficiaryRepo.findByCustomerId(id));
        List<Account> accounts = accountRepo.findByCustomerId(id);
        for (Account account : accounts) {
            beneficiaryRepo.deleteAll(beneficiaryRepo.findByBeneficiaryAccountId(account.getId()));
            transactionsRepo.deleteAll(transactionsRepo.findByAccountIdOrderByTransactionTimeDesc(account.getId()));
        }
        accountRepo.deleteAll(accounts);
        customerRepo.deleteById(id);
    }

    @Override
    public RegisterResponse registerCustomer(RegisterRequest request, String keycloakUserId) {
        Customer saved = customerRepo.save(new Customer(request.getPan(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber()));
        return new RegisterResponse(saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getPan(), saved.getPhoneNumber(), keycloakUserId);
    }
}
