package com.engfred.bookstore.controller;

import com.engfred.bookstore.dto.models.UserDto;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public interface UsersController {
    public ResponseEntity<?> findAllUsers(
            Integer page,
            Integer size,
            String sortField,
            Sort.Direction direction
    );

    public ResponseEntity<?> getUserById(UUID userId);
    public ResponseEntity<?> deleteUser(UUID userId);
    public ResponseEntity<?> updateUser(UUID userId, UserDto userDto);
}
