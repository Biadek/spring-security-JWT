package com.example.securityExample.service;

import com.example.securityExample.dto.ReaderDto;
import com.example.securityExample.exception.NotFoundException;
import com.example.securityExample.exception.NotAcceptableException;
import com.example.securityExample.model.Book;
import com.example.securityExample.model.Reader;
import com.example.securityExample.model.Role;
import com.example.securityExample.repository.BookRepository;
import com.example.securityExample.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReaderService {

    private ReaderRepository readerRepository;
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;

    public ReaderService(ReaderRepository readerRepository, BookRepository bookRepository) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
    }

    public ReaderDto modifyReader(ReaderDto readerDto) {
        if (userService.getRole().equals(Role.ROLE_ADMIN) && userService.getCurrentReader().getId() != readerDto.getId()) {
            throw new NotAcceptableException("Current user can not modify reader with id: " + readerDto.getId());
        }
        Reader reader = readerRepository.findById(readerDto.getId()).orElseThrow(() -> new NotFoundException("Reader not found: " + readerDto.getId()));
        reader.setAge(readerDto.getAge());
        reader.setName(readerDto.getName());
        return new ReaderDto(readerRepository.save(reader));
    }

    public List<ReaderDto> getAll() {
        return readerRepository.findAll().stream().map(ReaderDto::new).collect(Collectors.toList());
    }

    public ReaderDto getById(long id) {
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new NotFoundException("Reader not found"));
        return new ReaderDto(reader);
    }

    public void deleteReader(long id) {
        if (userService.getRole().equals(Role.ROLE_ADMIN) || userService.getCurrentReader().getId() == id) {
            readerRepository.deleteById(id);
        }
    }

    public void buyTheBook(long id, long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book not found: " + id));
        Reader reader = readerRepository.findById(id).orElseThrow(() -> new NotFoundException("Reader not found"));
        book.setOwner(reader);
        bookRepository.save(book);
    }

    public Reader createDefaultReader() {
        return readerRepository.save(new Reader());
    }
}
