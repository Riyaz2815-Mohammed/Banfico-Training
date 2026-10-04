package com.riyaz.banficotrainingprogram.payment.controller;

import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import com.riyaz.banficotrainingprogram.payment.dto.PaymentResponse;
import com.riyaz.banficotrainingprogram.payment.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PaymentResponse>>> getPayments(@RequestParam(required = false) UUID accountId, Authentication auth, @PageableDefault(size = 20, sort = "initiatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String email = (String) auth.getPrincipal();
        boolean isStaff = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_BANKMANAGER"));
        Page<PaymentResponse> result = paymentService.getPayments(email, accountId, isStaff, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.getTotalElements()));
        return ResponseEntity.ok().headers(headers).body(ApiResponse.ok("Payments retrieved", result));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(ApiResponse.ok("Payment retrieved", paymentService.getPayment(paymentId)));
    }
}
