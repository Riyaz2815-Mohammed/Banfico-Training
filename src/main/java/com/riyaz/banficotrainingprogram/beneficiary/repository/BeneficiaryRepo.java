package com.riyaz.banficotrainingprogram.beneficiary.repository;

import com.riyaz.banficotrainingprogram.beneficiary.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeneficiaryRepo extends JpaRepository<Beneficiary, UUID> {
    List<Beneficiary> findByCustomerId(UUID customerId);
    List<Beneficiary> findByBeneficiaryAccountId(UUID accountId);
}
