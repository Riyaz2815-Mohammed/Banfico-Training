package com.riyaz.banficotrainingprogram.beneficiary.controller;

import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryResponse;
import com.riyaz.banficotrainingprogram.beneficiary.service.BeneficiaryService;
import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BeneficiaryResponse>>> getBeneficiaries(@RequestParam(required = false) UUID customerId, @RequestParam(required = false) UUID accountId, Authentication auth) {
        String email = (String) auth.getPrincipal();
        boolean isStaff = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_BANKMANAGER"));
        return ResponseEntity.ok(ApiResponse.ok("Beneficiaries retrieved", beneficiaryService.getBeneficiaries(email, customerId, accountId, isStaff)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> addBeneficiary(@Valid @RequestBody BeneficiaryRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("Beneficiary added", beneficiaryService.addMyBeneficiary((String) auth.getPrincipal(), request)));
    }

    @PutMapping("/{beneficiaryId}")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> updateNickname(@PathVariable UUID beneficiaryId, @Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Beneficiary updated", beneficiaryService.updateBeneficiaryNickname(beneficiaryId, request)));
    }

    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<ApiResponse<Object>> deleteBeneficiary(@PathVariable UUID beneficiaryId, Authentication auth) {
        beneficiaryService.removeMyBeneficiary((String) auth.getPrincipal(), beneficiaryId);
        return ResponseEntity.ok(ApiResponse.ok("Beneficiary removed", null));
    }
}
