package com.engfred.bookstore.controller;

import com.engfred.bookstore.dto.models.BookDto;
import com.engfred.bookstore.dto.request.CreateBookRequest;
import com.engfred.bookstore.dto.request.UpdateBookRequest;
import com.engfred.bookstore.dto.response.PagingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.util.UUID;

public interface BooksController {
    ResponseEntity<BookDto> createBook(CreateBookRequest request) throws IOException;
    ResponseEntity<BookDto> updateBook(UUID bookId,UpdateBookRequest request) throws AccessDeniedException, IOException;
    ResponseEntity<PagingResult<BookDto>> getAllBooks(int pageNumber, int pageSize, String sortValue);
    ResponseEntity<PagingResult<BookDto>> getBooksByAuthor(UUID authorId, int pageNumber, int pageSize, String sortValue);
    ResponseEntity<BookDto> getBookById(UUID id);
    ResponseEntity<Void> deleteBook(UUID bookId) throws AccessDeniedException;
}
