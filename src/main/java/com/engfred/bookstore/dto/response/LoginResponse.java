package com.engfred.bookstore.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class LoginResponse {
    private UUID id;
    private String email;
    private String token;
}
