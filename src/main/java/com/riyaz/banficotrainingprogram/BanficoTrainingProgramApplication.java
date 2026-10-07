package com.riyaz.banficotrainingprogram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class BanficoTrainingProgramApplication {

    @PostConstruct
    public void init() { TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata")); }

    public static void main(String[] args) {
        SpringApplication.run(BanficoTrainingProgramApplication.class, args);
    }

}
