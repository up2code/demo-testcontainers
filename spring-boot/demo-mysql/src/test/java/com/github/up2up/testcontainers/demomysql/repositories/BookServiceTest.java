package com.github.up2up.testcontainers.demomysql.repositories;

import com.github.up2up.testcontainers.demomysql.entity.Book;
import com.github.up2up.testcontainers.demomysql.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class BookServiceTest {

    @Container
    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("init.sql");

    @Autowired
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void testGetBook() {
        Book book = bookRepository.findById(1L).get();

        assertThat(book)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "Book 1")
                .hasFieldOrPropertyWithValue("author", "Author 1")
                .hasFieldOrPropertyWithValue("isbn", "ISBN 1")
                .extracting("created")
                .isNotNull();
    }
}
