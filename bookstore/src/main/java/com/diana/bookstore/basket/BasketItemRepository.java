package com.diana.bookstore.basket;

import com.diana.bookstore.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
    Optional<BasketItem> findByBasketAndBook(Basket basket, Book book);
}
