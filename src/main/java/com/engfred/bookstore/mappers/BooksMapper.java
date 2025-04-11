package com.engfred.bookstore.mappers;

import com.engfred.bookstore.data.entities.Book;
import com.engfred.bookstore.dto.models.BookDto;
import com.engfred.bookstore.dto.request.CreateBookRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BooksMapper {
    private final ModelMapper modelMapper;

    public BookDto toDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    public Book toEntity(CreateBookRequest request) {
        return modelMapper.map(request, Book.class);
    }
}
