package com.engfred.bookstore.controller.impl;

import com.engfred.bookstore.controller.UsersController;
import com.engfred.bookstore.dto.models.UserDto;
import com.engfred.bookstore.dto.response.PagingResult;
import com.engfred.bookstore.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/bookstore/authors")
@RequiredArgsConstructor
public class UsersControllerImpl implements UsersController {

    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<?> findAllUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        try{
            log.debug("Fetching all authors with pagination...");
            final PagingResult<UserDto> authors = userService.getAllUsers(page, size, sortField);
            log.debug("Authors fetched successfully.");
            return ResponseEntity.ok(authors);
        } catch (DataAccessException ex) {
            log.error("Error fetching authors: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            log.error("Invalid pagination parameters: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") UUID userId) {
        try{
            log.debug("Fetching author with id: {}", userId);
            final UserDto author = userService.getUserById(userId);
            log.debug("Author fetched successfully.");
            return ResponseEntity.ok(author);
        }catch (DataAccessException ex) {
            log.error("Error fetching author: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }catch (EntityNotFoundException ex) {
            log.error("Author not found: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
        try{
            log.debug("Deleting author with id: {}", userId);
            userService.deleteUser(userId);
            log.debug("Author deleted successfully.");
            return ResponseEntity.noContent().build();
        }catch (DataAccessException ex) {
            log.error("Error deleting author: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }catch (AccessDeniedException ex ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") UUID userId, @RequestBody UserDto userDto) {
        try{
            log.debug("Updating author with id: {}", userId);
            final UserDto updatedAuthor = userService.updateUser(userId, userDto);
            log.debug("Author updated successfully.");
            return ResponseEntity.ok(updatedAuthor);
        }catch (DataAccessException ex) {
            log.error("Error updating author: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }catch (AccessDeniedException ex ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
        }
    }
}
