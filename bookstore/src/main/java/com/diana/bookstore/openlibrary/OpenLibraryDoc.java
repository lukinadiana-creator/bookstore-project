package com.diana.bookstore.openlibrary;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OpenLibraryDoc (
        @JsonProperty("cover_i")
        Integer coverId,

        @NotNull
        String title,

        @JsonProperty("author_name")
        List<String> author,

        @JsonProperty("first_publish_year")
        Integer year,

        @JsonProperty("subjects")
        List<String> genre
){}
