package com.engfred.bookstore.service.impl;


import com.engfred.bookstore.data.entities.User;
import com.engfred.bookstore.data.repository.UserRepository;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;
import com.engfred.bookstore.mappers.UserMapper;
import com.engfred.bookstore.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//repository -> service -> controller

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


    @Override
    public UserDto signup(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname().trim())
                .lastname(request.getLastname() != null ? request.getLastname().trim() : null)
                .email(request.getEmail().trim())
                .phoneNumber(request.getPhoneNumber().trim())
                .address(request.getAddress().trim())
                .bio(request.getBio() != null ? request.getBio().trim() : null)
                .password(passwordEncoder.encode(request.getPassword()))
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl().trim() : null)
                .gender(request.getGender())
                .build();
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return userMapper.toDto(user);
    }
}
