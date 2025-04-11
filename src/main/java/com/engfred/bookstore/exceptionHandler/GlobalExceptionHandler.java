package com.engfred.bookstore.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles validation errors from @Valid annotated request bodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Validation failed");
        responseBody.put("details", fieldErrors);

        return ResponseEntity.badRequest().body(responseBody);
    }

    // Handles unique constraint violations (e.g., duplicate email)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Data integrity violation";
        if (ex.getMostSpecificCause().getMessage().contains("email")) {
            message = "Email is already in use";
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Requested URL '" + ex.getRequestURL() + "' not found"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of("error", "HTTP method not allowed", "details", ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "File size exceeds 5MB limit"));
    }

    // Catch-all for other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Something went wrong!", "details", ex.getMessage()));
    }
}

