package com.engfred.bookstore.service;

import com.engfred.bookstore.dto.models.BookDto;
import com.engfred.bookstore.dto.request.CreateBookRequest;
import com.engfred.bookstore.dto.request.UpdateBookRequest;
import com.engfred.bookstore.dto.response.PagingResult;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.util.UUID;

public interface BooksService {
    BookDto createBook(CreateBookRequest request) throws IOException;
    BookDto updateBook(UpdateBookRequest request, UUID bookId) throws AccessDeniedException, IOException;
    PagingResult<BookDto> getAllBooks(int pageNumber, int pageSize, String sortField);
    PagingResult<BookDto> getBooksByAuthor(UUID authorId, int pageNumber, int pageSize, String sortField);
    PagingResult<BookDto> searchBooks(String keyword, int pageNumber, int pageSize, String sortField);
    BookDto getBookById(UUID id);
    void deleteBook(UUID bookId) throws AccessDeniedException;
}
