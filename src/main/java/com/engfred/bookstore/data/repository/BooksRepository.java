package com.engfred.bookstore.data.repository;

import com.engfred.bookstore.data.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BooksRepository extends JpaRepository<Book, UUID> {
    Page<Book> findAllByAuthorId(UUID authorId, Pageable pageable);

    @Query("""
        SELECT b FROM Book b
        WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(b.author.firstname) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(b.author.lastname) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<Book> searchBooks(@Param("query") String query, Pageable pageable);
}
