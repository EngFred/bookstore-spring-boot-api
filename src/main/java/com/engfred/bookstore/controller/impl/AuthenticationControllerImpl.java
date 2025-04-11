package com.engfred.bookstore.controller.impl;

import com.engfred.bookstore.controller.AuthenticationController;
import com.engfred.bookstore.data.entities.User;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;
import com.engfred.bookstore.dto.response.LoginResponse;
import com.engfred.bookstore.service.AuthenticationService;
import com.engfred.bookstore.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bookstore/auth")
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest request) {
        try {
            UserDto user = authenticationService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user); //MethodArgumentNotValidException
        } catch (DataIntegrityViolationException ex) {
            // This might occur if the email is already registered (assuming a unique constraint on email)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email is already in use"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try{
            UserDto authenticatedUser = authenticationService.authenticate(request);
            String jwt = jwtService.generateToken(authenticatedUser.getEmail());
            LoginResponse response = LoginResponse.builder().token(jwt).email(request.getEmail()).id(authenticatedUser.getId()).build();
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
