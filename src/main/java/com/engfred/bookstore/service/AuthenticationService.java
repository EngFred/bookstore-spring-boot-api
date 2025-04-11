package com.engfred.bookstore.service;

import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;

import java.io.IOException;

public interface AuthenticationService {
    public UserDto signup(RegisterRequest registerRequest) throws IOException;
    public UserDto authenticate(LoginRequest loginRequest);
}
