package com.engfred.bookstore.controller;

import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.UpdateUserRequest;
import com.engfred.bookstore.dto.response.PagingResult;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

public interface UsersController {
    ResponseEntity<PagingResult<UserDto>> findAllUsers(
            Integer page,
            Integer size,
            String sort,
            Sort.Direction direction
    );

    @GetMapping("/search")
    ResponseEntity<PagingResult<UserDto>> searchUsers(
            String keyword,
            int page,
            int size,
            String sort
    );

    ResponseEntity<UserDto> getUserById(UUID userId);
    ResponseEntity<Void> deleteUser() throws AccessDeniedException;
    ResponseEntity<UserDto> updateUser(UpdateUserRequest request) throws IOException;
}
