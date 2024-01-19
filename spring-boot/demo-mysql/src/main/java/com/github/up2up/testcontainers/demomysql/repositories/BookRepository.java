package com.github.up2up.testcontainers.demomysql.repositories;

import com.github.up2up.testcontainers.demomysql.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {
}
