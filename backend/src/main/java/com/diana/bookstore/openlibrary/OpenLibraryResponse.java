package com.diana.bookstore.openlibrary;

import java.util.List;

public record OpenLibraryResponse (
        List<OpenLibraryDoc> docs
){}