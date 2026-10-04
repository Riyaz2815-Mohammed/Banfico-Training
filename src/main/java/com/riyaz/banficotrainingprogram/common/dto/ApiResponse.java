package com.riyaz.banficotrainingprogram.common.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ok(String message, T data) { return new ApiResponse<>(200, message, data); }
    public static <T> ApiResponse<T> created(String message, T data) { return new ApiResponse<>(201, message, data); }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
