package com.engfred.bookstore.service;

import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.response.PagingResult;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

public interface UserService {
    public PagingResult<UserDto> getAllUsers(int pageNumber, int pageSize, String sortValue);
    UserDto getUserById(UUID userId);
    void deleteUser(UUID userId) throws AccessDeniedException;
    UserDto updateUser(UUID userId, UserDto userDto) throws AccessDeniedException;
}
