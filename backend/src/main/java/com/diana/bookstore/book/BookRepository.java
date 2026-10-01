package com.diana.bookstore.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByTitleAndAuthor(String title, String author);
    boolean existsByTitle(String title);
    void deleteByTitle(String title);
}
