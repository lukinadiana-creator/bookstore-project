package com.diana.bookstore.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OpenLibraryBookDto (
        Long id,

        String imageUrl,

        String title,

        String author,

        Integer year,

        Integer stock,

        BigDecimal price
) {}
