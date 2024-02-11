package com.github.up2up.testcontainers.mongodb.repositories;

import com.github.up2up.testcontainers.mongodb.entities.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, Long> {
}
