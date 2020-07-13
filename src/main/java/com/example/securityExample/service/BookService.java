package com.example.securityExample.service;

import com.example.securityExample.dto.BookDto;
import com.example.securityExample.exception.BookNotFoundException;
import com.example.securityExample.exception.ReaderNotFoundException;
import com.example.securityExample.model.Book;
import com.example.securityExample.model.Reader;
import com.example.securityExample.repository.BookRepository;
import com.example.securityExample.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private BookRepository bookRepository;
    private ReaderRepository readerRepository;

    public BookService(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    public BookDto addOrModifyBook(BookDto dto) {
        Reader reader = null;
        if (dto.getOwnerId() != null) {
            reader = readerRepository.findById(dto.getOwnerId()).orElseThrow(() -> new ReaderNotFoundException(dto.getOwnerId()));
        }
        Book book = dto.toEntity(reader);
        return new BookDto(bookRepository.save(book));
    }

    public List<BookDto> getAll() {
        return bookRepository.findAll().stream().map(BookDto::new).collect(Collectors.toList());
    }

    public List<BookDto> getAllByOwner(long ownerId) {
        Reader reader = readerRepository.findById(ownerId).orElseThrow(() -> new ReaderNotFoundException(ownerId));
        return reader.getBooks().stream().map(BookDto::new).collect(Collectors.toList());
    }

    public BookDto getById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return new BookDto(book);
    }

    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }
}
