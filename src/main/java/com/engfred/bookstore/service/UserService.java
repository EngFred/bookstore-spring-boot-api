package com.engfred.bookstore.service;

import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.UpdateUserRequest;
import com.engfred.bookstore.dto.response.PagingResult;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

public interface UserService {
    PagingResult<UserDto> getAllUsers(int pageNumber, int pageSize, String sortValue);

    PagingResult<UserDto> searchAuthors(String keyword, int pageNumber, int pageSize, String sortField);

    UserDto getUserById(UUID userId);
    void deleteUser() throws AccessDeniedException;
    UserDto updateUser(UpdateUserRequest request) throws IOException;
}
