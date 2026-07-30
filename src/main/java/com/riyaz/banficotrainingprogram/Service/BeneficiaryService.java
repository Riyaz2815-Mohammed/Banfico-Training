package com.riyaz.banficotrainingprogram.Service;

import com.riyaz.banficotrainingprogram.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryResponse;

import java.util.List;
import java.util.UUID;

public interface BeneficiaryService {
    BeneficiaryResponse createBeneficiary(UUID customerId, BeneficiaryRequest beneficiaryRequest);
    List<BeneficiaryResponse> getBeneficiaries(UUID customerId);
    BeneficiaryResponse updateBeneficiaryNickname(UUID customerId, UUID beneficiaryId, BeneficiaryRequest beneficiaryRequest);
    void deleteBeneficiary(UUID customerId, UUID beneficiaryId);
}
