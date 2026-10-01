package com.diana.bookstore.book;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/admin/import")
    public List<BookResponseDto> addBookByAuthor(@RequestParam String author) {
        return bookService.addBookByAuthor(author);
    }

    @GetMapping
    public List<BookResponseDto> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return bookService.getBooks(title, author, minPrice, maxPrice);
    }

    @GetMapping("/admin")
    public List<OpenLibraryBookDto> getAllInfoAboutBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {
        return bookService.getAllInfoAboutBooks(title, author);
    }

    @DeleteMapping("/admin")
    public void deleteBookById(@RequestParam Long id) {
        bookService.deleteBookById(id);
    }
}
