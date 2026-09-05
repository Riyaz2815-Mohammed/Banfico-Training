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
        Account acc = b.getBeneficiaryAccount();
        Customer holder = acc.getCustomer();
        return new BeneficiaryResponse(
                b.getId(),
                b.getCustomer().getId(),
                acc.getId(),
                acc.getAccountNo(),
                acc.getAccountType(),
                holder.getFirstName() + " " + holder.getLastName(),
                b.getNickname()
        );
    }

    @Override
    public BeneficiaryResponse createBeneficiary(UUID customerId, BeneficiaryRequest beneficiaryRequest) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        Account account = accountRepo.findById(beneficiaryRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + beneficiaryRequest.getAccountId()));
        Beneficiary beneficiary = new Beneficiary(customer, account, beneficiaryRequest.getNickname());
        return toResponse(beneficiaryRepo.save(beneficiary));
    }

    @Override
    public List<BeneficiaryResponse> getBeneficiaries(UUID customerId) {
        return beneficiaryRepo.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BeneficiaryResponse updateBeneficiaryNickname(UUID customerId, UUID beneficiaryId, BeneficiaryRequest beneficiaryRequest) {
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + beneficiaryId));
        beneficiary.setNickname(beneficiaryRequest.getNickname());
        return toResponse(beneficiaryRepo.save(beneficiary));
    }

    @Override
    public void deleteBeneficiary(UUID customerId, UUID beneficiaryId) {
        beneficiaryRepo.deleteById(beneficiaryId);
    }
}
