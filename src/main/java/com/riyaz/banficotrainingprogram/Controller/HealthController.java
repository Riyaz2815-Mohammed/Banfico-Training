package com.riyaz.banficotrainingprogram.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class HealthController {
    @GetMapping("/health")
    public String gethealth(){
        return "UP";
    }

    }

