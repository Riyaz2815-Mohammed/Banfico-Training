package com.riyaz.banficotrainingprogram.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthController {
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
}