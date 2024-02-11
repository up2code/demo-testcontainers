package com.github.up2up.testcontainers.mongodb.controllers;

import com.github.up2up.testcontainers.mongodb.entities.Book;
import com.github.up2up.testcontainers.mongodb.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/books")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
}
