package com.engfred.bookstore.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int pages;

    @Column(nullable = false, name = "published_at")
    private Date publishedAt;

    @Column(nullable = false)
    private String isbn;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "cover_image_public_id")
    private String coverImagePublicId;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    // Many books belong to one author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}

