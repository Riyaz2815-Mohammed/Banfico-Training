package com.riyaz.banficotrainingprogram.beneficiary.controller;

import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryResponse;
import com.riyaz.banficotrainingprogram.beneficiary.service.BeneficiaryService;
import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BeneficiaryResponse>>> getBeneficiaries(@RequestParam(required = false) UUID customerId, @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        return ResponseEntity.ok(ApiResponse.ok("Beneficiaries retrieved", beneficiaryService.getBeneficiaries(jwt.getClaimAsString("email"), customerId, roles.contains("admin") || roles.contains("BankManager"))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> addBeneficiary(@Valid @RequestBody BeneficiaryRequest request, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created("Beneficiary added", beneficiaryService.addMyBeneficiary(jwt.getClaimAsString("email"), request)));
    }

    @PutMapping("/{beneficiaryId}")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> updateNickname(@PathVariable UUID beneficiaryId, @Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Beneficiary updated", beneficiaryService.updateBeneficiaryNickname(beneficiaryId, request)));
    }

    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<ApiResponse<Object>> deleteBeneficiary(@PathVariable UUID beneficiaryId, @AuthenticationPrincipal Jwt jwt) {
        beneficiaryService.removeMyBeneficiary(jwt.getClaimAsString("email"), beneficiaryId);
        return ResponseEntity.ok(ApiResponse.ok("Beneficiary removed", null));
    }
}
