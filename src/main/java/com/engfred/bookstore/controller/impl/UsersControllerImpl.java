package com.engfred.bookstore.controller.impl;

import com.engfred.bookstore.controller.UsersController;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.request.UpdateUserRequest;
import com.engfred.bookstore.dto.response.PagingResult;
import com.engfred.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/bookstore/authors")
@RequiredArgsConstructor
public class UsersControllerImpl implements UsersController {

    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<PagingResult<UserDto>> findAllUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        final PagingResult<UserDto> authors = userService.getAllUsers(page, size, sort);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/search")
    @Override
    public ResponseEntity<PagingResult<UserDto>> searchUsers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstname") String sort
    ) {
        return ResponseEntity.ok(userService.searchAuthors(keyword, page, size, sort));
    }


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") UUID userId) {
        final UserDto author = userService.getUserById(userId);
        return ResponseEntity.ok(author);
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() throws AccessDeniedException {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateUser(@ModelAttribute UpdateUserRequest request) throws IOException {
        final UserDto updatedAuthor = userService.updateUser(request);
        return ResponseEntity.ok(updatedAuthor);
    }
}
