package com.example.securityExample.controller;

import com.example.securityExample.dto.ReaderDto;
import com.example.securityExample.service.ReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/reader")
public class ReaderController {

    private ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @PutMapping
    public ResponseEntity<ReaderDto> modifyData(@RequestBody ReaderDto readerDto) {
        return ResponseEntity.ok(readerService.modifyReader(readerDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReaderDto>> getAll() {
        return ResponseEntity.ok(readerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDto> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(readerService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable("id") long id) {
        readerService.deleteReader(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{readerId}/buy/{bookId}")
    public ResponseEntity<Void> buyTheBook(@PathVariable("readerId") long readerId, @PathVariable("bookId") long bookId) {
        readerService.buyTheBook(readerId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
