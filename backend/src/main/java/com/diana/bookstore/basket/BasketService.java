package com.diana.bookstore.basket;

import com.diana.bookstore.book.Book;
import com.diana.bookstore.book.BookRepository;
import com.diana.bookstore.book.BookResponseDto;
import com.diana.bookstore.user.User;
import com.diana.bookstore.user.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BasketService(
            BasketRepository basketRepository,
            BasketItemRepository basketItemRepository,
            UserRepository userRepository,
            BookRepository bookRepository)
    {
        this.basketRepository = basketRepository;
        this.basketItemRepository = basketItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public BasketResponseDto toDto(Basket basket) {
        List<BasketItemDto> items = basket.getItems().stream()
                .map(item -> new BasketItemDto(
                        new BookResponseDto(
                                item.getBook().getId(),
                                item.getBook().getImageUrl(),
                                item.getBook().getTitle(),
                                item.getBook().getAuthor(),
                                item.getBook().getYear(),
                                item.getBook().getPrice()
                        ),
                        item.getQuantity()
                ))
                .toList();

        BigDecimal totalPrice = basket.getItems().stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BasketResponseDto(items, totalPrice);
    }

    public BasketResponseDto getBasket(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User with email = " + email + "not found"));

        Basket basket = basketRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Basket not found"));

        return toDto(basket);
    }

    public void addBookToBasket(Long bookId, Integer quantity, String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User with email = " + email + "not found"));

        Basket basket = basketRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Basket not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<BasketItem> existingItem = basketItemRepository.findByBasketAndBook(basket, book);

        if (existingItem.isPresent()) {
            BasketItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            basketItemRepository.save(item);
        } else {
            BasketItem item = new BasketItem();
            item.setBasket(basket);
            item.setBook(book);
            item.setQuantity(quantity);
            basketItemRepository.save(item);
        }
    }

    public void removeBook(Long bookId, String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User with email = " + email + "not found"));

        Basket basket = basketRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Basket not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        BasketItem item = basketItemRepository.findByBasketAndBook(basket, book)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            basketItemRepository.save(item);
        } else {
            basketItemRepository.delete(item);
        }
    }
}
