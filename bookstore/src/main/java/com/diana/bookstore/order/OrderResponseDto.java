package com.diana.bookstore.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderResponseDto(
        List<OrderItemDto> items,
        BigDecimal totalAmount,
        LocalDate createdAt,
        OrderStatus status
) { }
