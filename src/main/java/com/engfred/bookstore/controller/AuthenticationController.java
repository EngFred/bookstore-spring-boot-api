package com.engfred.bookstore.controller;

import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;
import com.engfred.bookstore.dto.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface AuthenticationController {
    ResponseEntity<UserDto> signup(@Valid @RequestBody RegisterRequest request) throws IOException;
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request);
}
