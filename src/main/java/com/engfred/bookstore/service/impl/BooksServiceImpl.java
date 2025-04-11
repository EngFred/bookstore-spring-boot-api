package com.engfred.bookstore.service.impl;

import com.engfred.bookstore.data.entities.Book;
import com.engfred.bookstore.data.entities.ImageType;
import com.engfred.bookstore.data.entities.User;
import com.engfred.bookstore.data.repository.BooksRepository;
import com.engfred.bookstore.data.repository.UserRepository;
import com.engfred.bookstore.dto.models.BookDto;
import com.engfred.bookstore.dto.request.CreateBookRequest;
import com.engfred.bookstore.dto.request.UpdateBookRequest;
import com.engfred.bookstore.dto.response.CloudinaryUploadResult;
import com.engfred.bookstore.dto.response.PagingResult;
import com.engfred.bookstore.mappers.BooksMapper;
import com.engfred.bookstore.service.BooksService;
import com.engfred.bookstore.service.CloudinaryService;
import com.engfred.bookstore.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final UserRepository userRepository;
    private final BooksMapper bookMapper;
    private final CloudinaryService cloudinaryService;

    private static final Logger logger = LoggerFactory.getLogger(BooksServiceImpl.class);

    @Override
    public BookDto createBook(CreateBookRequest request) throws IOException {
        logger.info("Creating a book with title: {}", request.getTitle());

        String currentUserEmail = SecurityUtils.getCurrentUserEmail();

        User author = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> {
                    logger.error("Author not found for email: {}", currentUserEmail);
                    return new EntityNotFoundException("Author not found");
                });

        logger.info("Author found: {}", author.getEmail());

        // Upload image if present
        CloudinaryUploadResult uploadResult = null;
        if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
            uploadResult = cloudinaryService.uploadImage(request.getCoverImage(), ImageType.BOOK_COVER);
        }

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);

        if (uploadResult != null) {
            book.setCoverImageUrl(uploadResult.imageUrl());
            book.setCoverImagePublicId(uploadResult.publicId());
        }

        Book savedBook = booksRepository.save(book);
        logger.info("Book created successfully with ID: {}", savedBook.getId());

        return bookMapper.toDto(savedBook);
    }

    @Override
    public PagingResult<BookDto> getAllBooks(int pageNumber, int pageSize, String sortField) {
        logger.info("Fetching all books - Page: {}, Size: {}, Sort: {}", pageNumber, pageSize, sortField);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortField));
        final Page<Book> entities = booksRepository.findAll(pageable);

        logger.info("Fetched {} books", entities.getContent().size());
        return toPagingResult(entities);
    }

    @Override
    public PagingResult<BookDto> searchBooks(String keyword, int pageNumber, int pageSize, String sortField) {
        logger.info("Searching books with keyword: '{}'", keyword);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortField));
        Page<Book> booksPage = booksRepository.searchBooks(keyword.trim(), pageable);

        logger.info("Search result: {} books found", booksPage.getTotalElements());

        return toPagingResult(booksPage);
    }

    @Override
    public PagingResult<BookDto> getBooksByAuthor(UUID authorId, int pageNumber, int pageSize, String sortField) {
        logger.info("Fetching books for author ID: {} - Page: {}, Size: {}, Sort: {}", authorId, pageNumber, pageSize, sortField);

        userRepository.findById(authorId)
                .orElseThrow(() -> {
                    logger.error("Author not found with ID: {}", authorId);
                    return new EntityNotFoundException("Author not found");
                });

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortField));
        final Page<Book> entities = booksRepository.findAllByAuthorId(authorId, pageable);

        logger.info("Fetched {} books for author ID: {}", entities.getContent().size(), authorId);
        return toPagingResult(entities);
    }

    @Override
    public BookDto getBookById(UUID id) {
        logger.info("Fetching book by ID: {}", id);

        return booksRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new EntityNotFoundException("Book not found");
                });
    }

    @Override
    public void deleteBook(UUID bookId) {
        logger.info("Attempting to delete book with ID: {}", bookId);

        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", bookId);
                    return new EntityNotFoundException("Book not found");
                });

        if (!book.getAuthor().getEmail().equals(SecurityUtils.getCurrentUserEmail())) {
            logger.error("Access denied for deleting book with ID: {} by user: {}", bookId, SecurityUtils.getCurrentUserEmail());
            throw new AccessDeniedException("You are not allowed to delete this book!");
        }

        //Delete cover image from Cloudinary if it exists
        if (book.getCoverImagePublicId() != null && !book.getCoverImagePublicId().isEmpty()) {
            try {
                cloudinaryService.deleteImage(book.getCoverImagePublicId());
                logger.info("Deleted book cover image from Cloudinary: {}", book.getCoverImagePublicId());
            } catch (IOException e) {
                logger.warn("Failed to delete image from Cloudinary for book ID: {}. Reason: {}", bookId, e.getMessage());
            }
        }

        booksRepository.deleteById(bookId);
        logger.info("Book deleted successfully with ID: {}", bookId);
    }

    @Override
    public BookDto updateBook(UpdateBookRequest request, UUID bookId) throws IOException, AccessDeniedException {
        logger.info("Attempting to update book with ID: {}", bookId);

        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", bookId);
                    return new EntityNotFoundException("Book not found");
                });

        if (!book.getAuthor().getEmail().equals(SecurityUtils.getCurrentUserEmail())) {
            logger.error("Access denied for updating book with ID: {} by user: {}", bookId, SecurityUtils.getCurrentUserEmail());
            throw new AccessDeniedException("You are not allowed to update this book!");
        }

        logger.info("Updating book fields for book ID: {}", bookId);

        // Upload image if present
        CloudinaryUploadResult uploadResult = null;
        if (request.getCoverImage() != null && !request.getCoverImage().isEmpty()) {
            uploadResult = cloudinaryService.uploadImage(request.getCoverImage(), ImageType.BOOK_COVER);
        }

        book.setTitle(request.getTitle() != null && !request.getTitle().trim().equals(book.getTitle().trim()) ? request.getTitle().trim() : book.getTitle());
        book.setDescription(request.getDescription() != null && !request.getDescription().trim().equals(book.getDescription().trim()) ? request.getDescription().trim() : book.getDescription());
        book.setGenre(request.getGenre() != null && !request.getGenre().trim().equals(book.getGenre().trim()) ? request.getGenre().trim() : book.getGenre());
        book.setPages(request.getPages() > 0 ? request.getPages() : book.getPages());
        book.setPublishedAt(request.getPublishedAt() != null && !request.getPublishedAt().equals(book.getPublishedAt()) ? request.getPublishedAt() : book.getPublishedAt());
        book.setIsbn(request.getIsbn() != null && !request.getIsbn().trim().equals(book.getIsbn().trim()) ? request.getIsbn().trim() : book.getIsbn());

        if (uploadResult != null) {
            book.setCoverImageUrl(uploadResult.imageUrl());
            book.setCoverImagePublicId(uploadResult.publicId());
        }

        Book updatedBook = booksRepository.save(book);
        logger.info("Book updated successfully with ID: {}", updatedBook.getId());

        return bookMapper.toDto(updatedBook);
    }

    private PagingResult<BookDto> toPagingResult(Page<Book> entities) {
        logger.info("Mapping {} entities to DTOs", entities.getContent().size());
        final List<BookDto> entitiesDto = entities.stream().map(bookMapper::toDto).toList();
        logger.info("Mapping complete");
        return new PagingResult<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }
}
