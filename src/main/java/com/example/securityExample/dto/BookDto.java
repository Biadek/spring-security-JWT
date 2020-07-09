package com.example.securityExample.dto;

import com.example.securityExample.model.Book;
import com.example.securityExample.model.Reader;

public class BookDto {

    private long id;
    private String title;
    private Long ownerId;

    public BookDto() {
    }

    public BookDto(long id, String title, long ownerId) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
    }

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        if (book.getOwner() != null) {
            this.ownerId = book.getOwner().getId();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public Book toEntity(Reader reader) {
        Book book = new Book();
        book.setId(this.id);
        book.setTitle(this.title);
        book.setOwner(reader);
        return book;
    }
}
