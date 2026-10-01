package com.diana.bookstore.order;

import java.math.BigDecimal;

public record OrderItemDto(
       String title,
       String author,
       BigDecimal price,
       Integer quantity
) { }
