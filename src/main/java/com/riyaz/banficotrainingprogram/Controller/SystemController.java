package com.riyaz.banficotrainingprogram.Controller;

import com.riyaz.banficotrainingprogram.Service.SystemService;
import com.riyaz.banficotrainingprogram.dto.Healthresponse;
import com.riyaz.banficotrainingprogram.dto.InfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService =systemService ;
    }

    @GetMapping("/health")
    public String gethealth() {
        return "UP";
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
        return "LOVE"+ " " + body;

    }

    @GetMapping("/info")
    public ResponseEntity<InfoResponse> getInfo() {
        return ResponseEntity.ok(systemService.getInfo());
    }
}