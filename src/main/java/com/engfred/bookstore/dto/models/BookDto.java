package com.engfred.bookstore.dto.models;

import com.engfred.bookstore.data.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private UUID id;
    private String title;
    private String description;
    private String genre;
    private int pages;
    private Date publishedAt;
    private String isbn;
    private String coverImageUrl;
    private UserDto author;
}
