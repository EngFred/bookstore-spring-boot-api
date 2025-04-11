package com.engfred.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CreateBookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 300, message = "Title cannot exceed 300 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 1, message = "Pages must be at least 1")
    private int pages;

    @NotNull(message = "Published date is required")
    @PastOrPresent(message = "Published date must be in the past or present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date publishedAt;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private MultipartFile coverImage;
}
