package com.diana.bookstore.basket;

import com.diana.bookstore.book.BookResponseDto;

import java.math.BigDecimal;

public record BasketItemDto (
        BookResponseDto bookResponseDto,

        Integer quantity
){ }
