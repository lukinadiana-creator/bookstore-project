package com.diana.bookstore.book;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record BookResponseDto (
        Long id,

        String imageUrl,

        String title,

        String author,

        Integer year,

        BigDecimal price
) {}
