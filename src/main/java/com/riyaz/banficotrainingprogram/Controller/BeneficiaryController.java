package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.BeneficiaryService;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.dto.BeneficiaryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers/{customerId}/beneficiaries")
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;
    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }
    @PostMapping
    public ResponseEntity<BeneficiaryResponse> createBeneficiary(@PathVariable UUID customerId, @Valid @RequestBody BeneficiaryRequest beneficiaryRequest) {
        BeneficiaryResponse beneficiaryResponse = beneficiaryService.createBeneficiary(customerId, beneficiaryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryResponse);
    }

    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getBeneficiaries(@PathVariable UUID customerId) {
        List<BeneficiaryResponse> beneficiaryResponses = beneficiaryService.getBeneficiaries(customerId);
        return ResponseEntity.ok(beneficiaryResponses);
    }

    @PutMapping("/{beneficiaryId}")
    public ResponseEntity<BeneficiaryResponse> updateBeneficiaryNickname(@PathVariable UUID customerId, @PathVariable UUID beneficiaryId, @Valid @RequestBody BeneficiaryRequest beneficiaryRequest) {
        BeneficiaryResponse beneficiaryResponse = beneficiaryService.updateBeneficiaryNickname(customerId, beneficiaryId, beneficiaryRequest);
        return ResponseEntity.ok(beneficiaryResponse);
    }

    @DeleteMapping("/{beneficiaryId}")
    public String deleteBeneficiary(@PathVariable UUID customerId, @PathVariable UUID beneficiaryId) {
        beneficiaryService.deleteBeneficiary(customerId, beneficiaryId);
        return "Beneficiary " + " " + beneficiaryId + "has been Deleted";
    }
}
