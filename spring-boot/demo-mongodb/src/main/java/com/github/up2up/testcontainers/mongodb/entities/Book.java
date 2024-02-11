package com.github.up2up.testcontainers.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.time.LocalDateTime;

@Document(collection = "books")
@Data
public class Book {

    @Id
    private String id;

    private String title;

    private String author;

    private String isbn;

    private LocalDateTime created;

}