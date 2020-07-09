package com.example.securityExample.dto;

import com.example.securityExample.model.Book;
import com.example.securityExample.model.Reader;

import java.util.List;

public class ReaderDto {

    private long id;
    private String name;
    private int age;

    public ReaderDto(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public ReaderDto(Reader reader) {
        this.id = reader.getId();
        this.name = reader.getName();
        this.age = reader.getAge();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Reader toEntity(List<Book> books) {
        Reader reader = new Reader();
        reader.setId(this.id);
        reader.setName(this.name);
        reader.setAge(this.age);
        reader.setBooks(books);
        return reader;
    }
}
