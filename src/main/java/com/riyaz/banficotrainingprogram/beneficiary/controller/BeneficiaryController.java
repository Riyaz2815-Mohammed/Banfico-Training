package com.riyaz.banficotrainingprogram.beneficiary.controller;

import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryRequest;
import com.riyaz.banficotrainingprogram.beneficiary.dto.BeneficiaryResponse;
import com.riyaz.banficotrainingprogram.beneficiary.service.BeneficiaryService;
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
    public ResponseEntity<List<BeneficiaryResponse>> getBeneficiaries(@RequestParam(required = false) UUID customerId, @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        if (isStaff && customerId != null) return ResponseEntity.ok(beneficiaryService.getBeneficiaries(customerId));
        return ResponseEntity.ok(beneficiaryService.getMyBeneficiaries(jwt.getClaimAsString("email")));
    }

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> addBeneficiary(@RequestParam(required = false) UUID customerId, @Valid @RequestBody BeneficiaryRequest request, @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        if (isStaff && customerId != null) return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryService.createBeneficiary(customerId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficiaryService.addMyBeneficiary(jwt.getClaimAsString("email"), request));
    }

    @PutMapping("/{beneficiaryId}")
    public ResponseEntity<BeneficiaryResponse> updateNickname(@PathVariable UUID beneficiaryId, @Valid @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(beneficiaryService.updateBeneficiaryNickname(beneficiaryId, request));
    }

    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable UUID beneficiaryId, @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        if (isStaff) beneficiaryService.deleteBeneficiary(beneficiaryId);
        else beneficiaryService.removeMyBeneficiary(jwt.getClaimAsString("email"), beneficiaryId);
        return ResponseEntity.noContent().build();
    }
}
