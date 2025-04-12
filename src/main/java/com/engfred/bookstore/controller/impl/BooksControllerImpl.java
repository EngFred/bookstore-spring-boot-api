package com.engfred.bookstore.controller.impl;

import com.engfred.bookstore.controller.BooksController;
import com.engfred.bookstore.dto.models.BookDto;
import com.engfred.bookstore.dto.request.CreateBookRequest;
import com.engfred.bookstore.dto.request.UpdateBookRequest;
import com.engfred.bookstore.dto.response.PagingResult;
import com.engfred.bookstore.service.BooksService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksControllerImpl implements BooksController {

    private final BooksService booksService;

    @Override
    @GetMapping
    public ResponseEntity<PagingResult<BookDto>> getAllBooks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort
    ) {
        PagingResult<BookDto> books = booksService.getAllBooks(page, size, sort);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<PagingResult<BookDto>> searchBooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort
    ) {
        return ResponseEntity.ok(booksService.searchBooks(keyword, page, size, sort));
    }

    @Override
    @GetMapping("/author/{authorId}")
    public ResponseEntity<PagingResult<BookDto>> getBooksByAuthor(
            @PathVariable UUID authorId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort
    ) {
        PagingResult<BookDto> books = booksService.getBooksByAuthor(authorId, page, size, sort);
        return ResponseEntity.ok(books);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookDto> createBook(@Valid @ModelAttribute CreateBookRequest request) throws IOException {
        BookDto bookDto = booksService.createBook(request);
        return new ResponseEntity<>(bookDto, HttpStatus.CREATED);
    }

    @Override
    @PutMapping(value = "/{bookId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookDto> updateBook(
            @PathVariable UUID bookId,
            @Valid @ModelAttribute UpdateBookRequest request
    ) throws AccessDeniedException, IOException {
        BookDto updatedBook = booksService.updateBook(request, bookId);
        return ResponseEntity.ok(updatedBook);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable UUID id) {
        BookDto bookDto = booksService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @Override
    @DeleteMapping("/{bookId}/delete")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID bookId) throws AccessDeniedException {
        booksService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
}
