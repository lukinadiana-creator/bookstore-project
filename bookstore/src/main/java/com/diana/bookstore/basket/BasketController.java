package com.diana.bookstore.basket;

import com.diana.bookstore.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping
    public BasketResponseDto getBasket(Authentication authentication) {
        return basketService.getBasket(authentication.getName());
    }

    @PostMapping("/items")
    public void addBookToBasket(
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication authentication
    ) {
        basketService.addBookToBasket(bookId, quantity, authentication.getName());
    }

    @DeleteMapping("/items")
    public void removeBook(
            @RequestParam Long bookId,
            Authentication authentication
    ) {
        basketService.removeBook(bookId, authentication.getName());
    }
}
