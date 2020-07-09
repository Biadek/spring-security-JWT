package com.example.securityExample.controller;

import com.example.securityExample.dto.BookDto;
import com.example.securityExample.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/book")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> addOrUpdate(@RequestBody BookDto bookDto) {
        return new ResponseEntity(bookService.addOrModifyBook(bookDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAll() {
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping("/byOwner/{id}")
    public ResponseEntity<List<BookDto>> getAllByOwner(@PathVariable("id") long id) {
        return ResponseEntity.ok(bookService.getAllByOwner(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
