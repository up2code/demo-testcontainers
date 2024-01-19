USE test;

CREATE TABLE books (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       title VARCHAR(255),
                       author VARCHAR(255),
                       isbn VARCHAR(255),
                       created DATETIME,
                       PRIMARY KEY (id)
);
INSERT INTO books (title, author, isbn, created) VALUES('Book 1', 'Author 1', 'ISBN 1', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 2', 'Author 2', 'ISBN 2', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 3', 'Author 3', 'ISBN 3', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 4', 'Author 4', 'ISBN 4', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 5', 'Author 5', 'ISBN 5', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 6', 'Author 6', 'ISBN 6', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 7', 'Author 7', 'ISBN 7', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 8', 'Author 8', 'ISBN 8', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 9', 'Author 9', 'ISBN 9', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 10', 'Author 10', 'ISBN 10', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 11', 'Author 11', 'ISBN 11', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 12', 'Author 12', 'ISBN 12', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 13', 'Author 13', 'ISBN 13', NOW());
INSERT INTO books (title, author, isbn, created) VALUES('Book 14', 'Author 14', 'ISBN 14', NOW());