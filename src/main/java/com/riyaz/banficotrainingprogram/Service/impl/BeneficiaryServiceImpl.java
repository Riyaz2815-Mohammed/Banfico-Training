package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Beneficiary;
import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Service.BeneficiaryService;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryResponse;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
import com.riyaz.banficotrainingprogram.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.repository.BeneficiaryRepo;
import com.riyaz.banficotrainingprogram.repository.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {
    private final BeneficiaryRepo beneficiaryRepo;
    private final CustomerRepo customerRepo;
    private final AccountRepo accountRepo;

    public BeneficiaryServiceImpl(BeneficiaryRepo beneficiaryRepo, CustomerRepo customerRepo, AccountRepo accountRepo) {
        this.beneficiaryRepo = beneficiaryRepo;
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
    }

    private BeneficiaryResponse toResponse(Beneficiary b) {
        return new BeneficiaryResponse(b.getId(), b.getCustomer().getId(), b.getBeneficiaryAccount().getId(), b.getBeneficiaryAccount().getAccountNo(), b.getBeneficiaryAccount().getAccountType(), b.getBeneficiaryAccount().getCustomer().getFirstName() + " " + b.getBeneficiaryAccount().getCustomer().getLastName(), b.getNickname());
    }

    @Override
    public List<BeneficiaryResponse> getBeneficiaries(UUID customerId) {
        return beneficiaryRepo.findByCustomerId(customerId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<BeneficiaryResponse> getMyBeneficiaries(String email) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        return beneficiaryRepo.findByCustomerId(customer.getId()).stream().map(this::toResponse).toList();
    }

    @Override
    public BeneficiaryResponse createBeneficiary(UUID customerId, BeneficiaryRequest request) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        Account account = accountRepo.findById(request.getAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));
        return toResponse(beneficiaryRepo.save(new Beneficiary(customer, account, request.getNickname())));
    }

    @Override
    public BeneficiaryResponse addMyBeneficiary(String email, BeneficiaryRequest request) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        Account account = accountRepo.findById(request.getAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));
        return toResponse(beneficiaryRepo.save(new Beneficiary(customer, account, request.getNickname())));
    }

    @Override
    public BeneficiaryResponse updateBeneficiaryNickname(UUID beneficiaryId, BeneficiaryRequest request) {
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId).orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + beneficiaryId));
        beneficiary.setNickname(request.getNickname());
        return toResponse(beneficiaryRepo.save(beneficiary));
    }

    @Override
    public void deleteBeneficiary(UUID beneficiaryId) {
        beneficiaryRepo.findById(beneficiaryId).orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + beneficiaryId));
        beneficiaryRepo.deleteById(beneficiaryId);
    }

    @Override
    public void removeMyBeneficiary(String email, UUID beneficiaryId) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId).orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + beneficiaryId));
        if (!beneficiary.getCustomer().getId().equals(customer.getId())) throw new ResourceNotFoundException("Beneficiary not found with id: " + beneficiaryId);
        beneficiaryRepo.deleteById(beneficiaryId);
    }
}
