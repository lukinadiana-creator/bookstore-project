package com.diana.bookstore.basket;

import java.math.BigDecimal;
import java.util.List;

public record BasketResponseDto(
        List<BasketItemDto> items,
        BigDecimal totalPrice
) { }
