package com.engfred.bookstore.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
