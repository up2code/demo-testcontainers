package com.github.up2up.testcontainers.mongodb.repositories;

import com.github.up2up.testcontainers.mongodb.entities.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class BookRepositoryTest {
    @Container
    public static final GenericContainer<?> container = new GenericContainer<>(
            DockerImageName.parse("mongo:7.0.5"))
            .withExposedPorts(27017)
            .withCopyFileToContainer(MountableFile.forClasspathResource("init.js"), "/docker-entrypoint-initdb.d/init-script.js")
            .withLogConsumer(outputFrame -> System.out.println(outputFrame.getUtf8String()))
            ;

    @Autowired
    BookRepository bookRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", container::getHost);
        registry.add("spring.data.mongodb.port", container::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "test-user");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "library");
    }

    @Test
    void testGetBook() {
        List<Book> books = bookRepository.findAll();
        assertThat(books)
                .isNotNull()
                .hasSize(3);
    }
}
