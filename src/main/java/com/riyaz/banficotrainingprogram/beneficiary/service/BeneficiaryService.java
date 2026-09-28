package com.riyaz.banficotrainingprogram.beneficiary.service;

import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryResponse;

import java.util.List;
import java.util.UUID;

public interface BeneficiaryService {
    List<BeneficiaryResponse> getBeneficiaries(UUID customerId);
    List<BeneficiaryResponse> getMyBeneficiaries(String email);
    BeneficiaryResponse createBeneficiary(UUID customerId, BeneficiaryRequest request);
    BeneficiaryResponse addMyBeneficiary(String email, BeneficiaryRequest request);
    BeneficiaryResponse updateBeneficiaryNickname(UUID beneficiaryId, BeneficiaryRequest request);
    void deleteBeneficiary(UUID beneficiaryId);
    void removeMyBeneficiary(String email, UUID beneficiaryId);
}
