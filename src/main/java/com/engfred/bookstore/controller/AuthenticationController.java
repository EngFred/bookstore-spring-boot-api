package com.engfred.bookstore.controller;

import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

////Flexible return types to handle error maps
public interface AuthenticationController {
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest request);
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request);
}
