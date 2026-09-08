package com.riyaz.banficotrainingprogram.exception;

import com.riyaz.banficotrainingprogram.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
        return error(HttpStatus.BAD_REQUEST, "Insufficient Balance", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return error(HttpStatus.BAD_REQUEST, "Validation Failed", message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        String friendly;
        if (msg != null && msg.contains("unique") || msg != null && msg.contains("duplicate")) {
            friendly = "A customer with this email or PAN already exists.";
        } else if (msg != null && msg.contains("too long") || msg != null && msg.contains("value too long")) {
            friendly = "One or more fields exceed the allowed length.";
        } else {
            friendly = "Invalid data — please check your input.";
        }
        return error(HttpStatus.BAD_REQUEST, "Invalid Data", friendly);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakClientError(HttpClientErrorException ex) {
        if (ex.getStatusCode().value() == 409) {
            return error(HttpStatus.CONFLICT, "User Already Exists", "A user with this username or email already exists in Keycloak.");
        }
        return error(HttpStatus.BAD_REQUEST, "Registration Error", "Authentication service rejected the request: " + ex.getStatusText());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientError(RestClientException ex) {
        return error(HttpStatus.BAD_GATEWAY, "Service Unavailable", "Could not connect to authentication service. Please try again.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), error, message, LocalDateTime.now()));
    }
}
