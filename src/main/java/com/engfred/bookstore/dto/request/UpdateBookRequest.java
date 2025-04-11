package com.engfred.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class UpdateBookRequest {

    @Size(max = 300, message = "Title cannot exceed 300 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String genre;

    private int pages;

    @PastOrPresent(message = "Published date must be in the past or present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date publishedAt;

    private String isbn;

    private MultipartFile coverImage;
}
