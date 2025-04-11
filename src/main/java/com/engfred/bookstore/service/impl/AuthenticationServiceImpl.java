package com.engfred.bookstore.service.impl;


import com.engfred.bookstore.data.entities.ImageType;
import com.engfred.bookstore.data.entities.User;
import com.engfred.bookstore.data.repository.UserRepository;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.LoginRequest;
import com.engfred.bookstore.dto.request.RegisterRequest;
import com.engfred.bookstore.dto.response.CloudinaryUploadResult;
import com.engfred.bookstore.mappers.UserMapper;
import com.engfred.bookstore.service.AuthenticationService;
import com.engfred.bookstore.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

//repository -> service -> controller

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;


    @Override
    public UserDto signup(RegisterRequest request) throws IOException {
        User user = User.builder()
                .firstname(request.getFirstname().trim())
                .lastname(request.getLastname() != null ? request.getLastname().trim() : null)
                .email(request.getEmail().trim())
                .phoneNumber(request.getPhoneNumber().trim())
                .address(request.getAddress().trim())
                .bio(request.getBio() != null ? request.getBio().trim() : null)
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .build();

        // Upload image if present
        CloudinaryUploadResult uploadResult = null;
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            uploadResult = cloudinaryService.uploadImage(request.getProfileImage(), ImageType.USER_IMAGE);
        }

        if (uploadResult != null) {
            user.setImageUrl(uploadResult.imageUrl());
            user.setProfileImagePublicId(uploadResult.publicId());
        }

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
