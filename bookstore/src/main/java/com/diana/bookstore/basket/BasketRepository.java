package com.diana.bookstore.basket;

import com.diana.bookstore.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByUser(User user);
}
