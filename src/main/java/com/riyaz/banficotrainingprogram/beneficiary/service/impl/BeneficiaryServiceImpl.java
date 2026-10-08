package com.riyaz.banficotrainingprogram.beneficiary.service.impl;

import com.riyaz.banficotrainingprogram.account.entity.Account;
import com.riyaz.banficotrainingprogram.account.repository.AccountRepo;
import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryResponse;
import com.riyaz.banficotrainingprogram.beneficiary.entity.Beneficiary;
import com.riyaz.banficotrainingprogram.beneficiary.repository.BeneficiaryRepo;
import com.riyaz.banficotrainingprogram.beneficiary.service.BeneficiaryService;
import com.riyaz.banficotrainingprogram.customer.entity.Customer;
import com.riyaz.banficotrainingprogram.customer.repository.CustomerRepo;
import com.riyaz.banficotrainingprogram.exception.ResourceNotFoundException;
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
        Account src = b.getSourceAccount();
        return new BeneficiaryResponse(b.getId(), b.getCustomer().getId(), b.getBeneficiaryAccount().getId(), b.getBeneficiaryAccount().getAccountNo(), b.getBeneficiaryAccount().getAccountType(), b.getBeneficiaryAccount().getCustomer().getFirstName() + " " + b.getBeneficiaryAccount().getCustomer().getLastName(), b.getNickname(), src != null ? src.getId() : null, src != null ? src.getAccountNo() : null);
    }

    @Override
    public List<BeneficiaryResponse> getBeneficiaries(String email, UUID customerId, UUID accountId, boolean isStaff) {
        if (isStaff && accountId != null) return beneficiaryRepo.findBySourceAccountId(accountId).stream().map(this::toResponse).toList();
        if (isStaff && customerId != null) return beneficiaryRepo.findByCustomerId(customerId).stream().map(this::toResponse).toList();
        if (isStaff) return beneficiaryRepo.findAll().stream().map(this::toResponse).toList();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        if (accountId != null) return beneficiaryRepo.findBySourceAccountId(accountId).stream().filter(b -> b.getCustomer().getId().equals(customer.getId())).map(this::toResponse).toList();
        return beneficiaryRepo.findByCustomerId(customer.getId()).stream().map(this::toResponse).toList();
    }

    @Override
    public BeneficiaryResponse createBeneficiary(UUID customerId, BeneficiaryRequest request) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        Account account = accountRepo.findById(request.getAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));
        Account source = request.getSourceAccountId() != null ? accountRepo.findById(request.getSourceAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getSourceAccountId())) : null;
        return toResponse(beneficiaryRepo.save(new Beneficiary(customer, source, account, request.getNickname())));
    }

    @Override
    public BeneficiaryResponse addMyBeneficiary(String email, BeneficiaryRequest request) {
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No customer record found for email: " + email));
        if (request.getSourceAccountId() == null) throw new ResourceNotFoundException("Source account is required");
        Account source = accountRepo.findById(request.getSourceAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getSourceAccountId()));
        if (!source.getCustomer().getId().equals(customer.getId())) throw new ResourceNotFoundException("Account not found with id: " + request.getSourceAccountId());
        Account account = accountRepo.findById(request.getAccountId()).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getAccountId()));
        return toResponse(beneficiaryRepo.save(new Beneficiary(customer, source, account, request.getNickname())));
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
