package com.riyaz.banficotrainingprogram.system.dto;

import java.time.LocalDateTime;

public class HealthResponse {
    private String status;
    private LocalDateTime timestamp;

    public HealthResponse(String status, LocalDateTime timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
