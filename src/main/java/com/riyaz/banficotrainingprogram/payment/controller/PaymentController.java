package com.riyaz.banficotrainingprogram.payment.controller;

import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getPayments(@RequestParam(required = false) UUID accountId, @AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
        boolean isStaff = roles.contains("admin") || roles.contains("BankManager");
        if (isStaff && accountId != null) return ResponseEntity.ok(paymentService.getPaymentsByAccount(accountId));
        if (isStaff) return ResponseEntity.ok(paymentService.getAllPayments());
        return ResponseEntity.ok(paymentService.getMyPayments(jwt.getClaimAsString("email")));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }
}
