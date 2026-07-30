package com.riyaz.banficotrainingprogram.Service.impl;

import com.riyaz.banficotrainingprogram.Entity.Account;
import com.riyaz.banficotrainingprogram.Entity.Beneficiary;
import com.riyaz.banficotrainingprogram.Entity.Customer;
import com.riyaz.banficotrainingprogram.Service.BeneficiaryService;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryResponse;
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

    @Override
    public BeneficiaryResponse createBeneficiary(UUID customerId, BeneficiaryRequest beneficiaryRequest) {
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if (customer == null) { return null; }
        Account account = accountRepo.findById(beneficiaryRequest.getAccountId()).orElse(null);
        if (account == null) { return null; }
        Beneficiary beneficiary = new Beneficiary(customer, account, beneficiaryRequest.getNickname());
        Beneficiary savedBeneficiary = beneficiaryRepo.save(beneficiary);
        return new BeneficiaryResponse(savedBeneficiary.getId(), savedBeneficiary.getCustomer().getId(), savedBeneficiary.getBeneficiaryAccount().getId(), savedBeneficiary.getNickname());
    }

    @Override
    public List<BeneficiaryResponse> getBeneficiaries(UUID customerId) {
        List<Beneficiary> beneficiaries = beneficiaryRepo.findByCustomerId(customerId);
        return beneficiaries.stream().map(beneficiary -> new BeneficiaryResponse(
                beneficiary.getId(), beneficiary.getCustomer().getId(),
                beneficiary.getBeneficiaryAccount().getId(), beneficiary.getNickname()
        )).toList();
    }

    @Override
    public BeneficiaryResponse updateBeneficiaryNickname(UUID customerId, UUID beneficiaryId, BeneficiaryRequest beneficiaryRequest) {
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId).orElse(null);
        if (beneficiary == null) { return null; }
        beneficiary.setNickname(beneficiaryRequest.getNickname());
        Beneficiary updatedBeneficiary = beneficiaryRepo.save(beneficiary);
        return new BeneficiaryResponse(updatedBeneficiary.getId(), updatedBeneficiary.getCustomer().getId(), updatedBeneficiary.getBeneficiaryAccount().getId(), updatedBeneficiary.getNickname());
    }

    @Override
    public void deleteBeneficiary(UUID customerId, UUID beneficiaryId) {
        beneficiaryRepo.deleteById(beneficiaryId);
    }
}
