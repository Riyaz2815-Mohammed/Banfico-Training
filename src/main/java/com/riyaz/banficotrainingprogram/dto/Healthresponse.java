package com.riyaz.banficotrainingprogram.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Healthresponse {
    private String status;
    private LocalDateTime timestamp;

    public Healthresponse(String status, LocalDateTime timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }

    public String getStatus() {
        return status;
    }

//    public void setStatus(String status) {
//        this.status = status;
//    }
}
