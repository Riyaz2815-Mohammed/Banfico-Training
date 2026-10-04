package com.riyaz.banficotrainingprogram.system.controller;

import com.riyaz.banficotrainingprogram.common.dto.ApiResponse;
import com.riyaz.banficotrainingprogram.system.dto.HealthResponse;
import com.riyaz.banficotrainingprogram.system.dto.InfoResponse;
import com.riyaz.banficotrainingprogram.system.service.SystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SystemController {
    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<HealthResponse>> getHealth() {
        return ResponseEntity.ok(ApiResponse.ok("Health check", systemService.getHealth()));
    }

    @GetMapping("/love")
    public String love() {
        return "LOVE";
    }

    @GetMapping("/")
    public String index() {
        return "hello world";
    }

    @PostMapping("/love")
    public String post(@RequestBody String body) {
        return "LOVE" + " " + body;
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<InfoResponse>> getInfo() {
        return ResponseEntity.ok(ApiResponse.ok("System info", systemService.getInfo()));
    }
}
