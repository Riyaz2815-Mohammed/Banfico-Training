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

    @Override
    public BeneficiaryResponse createBeneficiary(UUID customerId, BeneficiaryRequest beneficiaryRequest) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        Account account = accountRepo.findById(beneficiaryRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + beneficiaryRequest.getAccountId()));
        Beneficiary beneficiary = new Beneficiary(customer, account, beneficiaryRequest.getNickname());
        Beneficiary savedBeneficiary = beneficiaryRepo.save(beneficiary);
        String accountHolderName = savedBeneficiary.getBeneficiaryAccount().getCustomer().getFirstName() + " " + savedBeneficiary.getBeneficiaryAccount().getCustomer().getLastName();
        return new BeneficiaryResponse(savedBeneficiary.getId(), savedBeneficiary.getCustomer().getId(), savedBeneficiary.getBeneficiaryAccount().getId(), savedBeneficiary.getBeneficiaryAccount().getAccountNo(), savedBeneficiary.getBeneficiaryAccount().getAccountType(), accountHolderName, savedBeneficiary.getNickname());
    }

    @Override
    public List<BeneficiaryResponse> getBeneficiaries(UUID customerId) {
        List<Beneficiary> beneficiaries = beneficiaryRepo.findByCustomerId(customerId);
        return beneficiaries.stream().map(beneficiary -> {
            String accountHolderName = beneficiary.getBeneficiaryAccount().getCustomer().getFirstName() + " " + beneficiary.getBeneficiaryAccount().getCustomer().getLastName();
            return new BeneficiaryResponse(
                    beneficiary.getId(), beneficiary.getCustomer().getId(),
                    beneficiary.getBeneficiaryAccount().getId(), beneficiary.getBeneficiaryAccount().getAccountNo(),
                    beneficiary.getBeneficiaryAccount().getAccountType(), accountHolderName, beneficiary.getNickname());
        }).toList();
    }

    @Override
    public BeneficiaryResponse updateBeneficiaryNickname(UUID customerId, UUID beneficiaryId, BeneficiaryRequest beneficiaryRequest) {
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found with id: " + beneficiaryId));
        beneficiary.setNickname(beneficiaryRequest.getNickname());
        Beneficiary updatedBeneficiary = beneficiaryRepo.save(beneficiary);
        String accountHolderName = updatedBeneficiary.getBeneficiaryAccount().getCustomer().getFirstName() + " " + updatedBeneficiary.getBeneficiaryAccount().getCustomer().getLastName();
        return new BeneficiaryResponse(updatedBeneficiary.getId(), updatedBeneficiary.getCustomer().getId(), updatedBeneficiary.getBeneficiaryAccount().getId(), updatedBeneficiary.getBeneficiaryAccount().getAccountNo(), updatedBeneficiary.getBeneficiaryAccount().getAccountType(), accountHolderName, updatedBeneficiary.getNickname());
    }

    @Override
    public void deleteBeneficiary(UUID customerId, UUID beneficiaryId) {
        beneficiaryRepo.deleteById(beneficiaryId);
    }
}
