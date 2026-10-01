package com.diana.bookstore.basket;

import com.diana.bookstore.book.Book;
import jakarta.persistence.*;

@Entity
public class BasketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Basket basket;

    @ManyToOne
    private Book book;

    @Column
    private Integer quantity;

    public BasketItem() {};

    public BasketItem(Long id, Basket basket, Book book, Integer quantity) {
        this.id = id;
        this.basket = basket;
        this.book = book;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
