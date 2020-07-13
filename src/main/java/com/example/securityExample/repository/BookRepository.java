package com.example.securityExample.repository;

import com.example.securityExample.model.Book;
import com.example.securityExample.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
