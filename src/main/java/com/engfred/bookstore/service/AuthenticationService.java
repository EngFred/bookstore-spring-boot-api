package com.engfred.bookstore.service;

import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;

public interface AuthenticationService {
    public UserDto signup(RegisterRequest registerRequest);
    public UserDto authenticate(LoginRequest loginRequest);
}
