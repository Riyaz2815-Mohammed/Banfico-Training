package com.riyaz.banficotrainingprogram.system.controller;

import com.riyaz.banficotrainingprogram.system.dto.HealthResponse;
import com.riyaz.banficotrainingprogram.system.dto.InfoResponse;
import com.riyaz.banficotrainingprogram.system.service.SystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SystemController {
    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealth() {
        return ResponseEntity.ok(systemService.getHealth());
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
    public ResponseEntity<InfoResponse> getInfo() {
        return ResponseEntity.ok(systemService.getInfo());
    }
}
