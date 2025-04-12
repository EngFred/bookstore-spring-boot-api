package com.engfred.bookstore.controller.impl;

import com.engfred.bookstore.controller.AuthenticationController;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;
import com.engfred.bookstore.dto.response.LoginResponse;
import com.engfred.bookstore.service.AuthenticationService;
import com.engfred.bookstore.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Override
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> signup(@Valid @ModelAttribute RegisterRequest request) throws IOException {
        UserDto user = authenticationService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        UserDto authenticatedUser = authenticationService.authenticate(request);
        String jwt = jwtService.generateToken(authenticatedUser.getEmail());
        LoginResponse response = LoginResponse.builder().token(jwt).email(request.getEmail()).build();
        return ResponseEntity.ok(response);
    }
}
