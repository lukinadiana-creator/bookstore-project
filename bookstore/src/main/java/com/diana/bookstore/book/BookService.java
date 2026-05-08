package com.diana.bookstore.book;

import com.diana.bookstore.openlibrary.OpenLibraryService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookService {
    private final BookRepository repository;
    private final OpenLibraryService openLibraryService;

    public BookService (BookRepository repository, OpenLibraryService openLibraryService) {
        this.repository = repository;
        this.openLibraryService = openLibraryService;
    }

    public BookResponseDto toResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getImageUrl(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getPrice()
        );
    }

    public Book toEntity(OpenLibraryBookDto dto) {
        return repository
                .findByTitleAndAuthor(dto.title(), dto.author())
                .orElseGet(() -> new Book(
                null,
                dto.imageUrl(),
                dto.title(),
                dto.author(),
                dto.year(),
                generateStock(),
                generatePrice()
        ));
    }

    public List<BookResponseDto> addBookByAuthor(String author) {
        List<OpenLibraryBookDto> books = openLibraryService.searchBook(author);

        return books.stream()
                .map(this::toEntity)
                .map(repository::save)
                .map(this::toResponseDto)
                .toList();
    }

    public Integer generateStock() {
        return ThreadLocalRandom.current().nextInt(0, 20);
    }

    public BigDecimal generatePrice() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(400.0, 2500.0))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public List<BookResponseDto> getBooks(String title, String author, Double minPrice, Double maxPrice) {
        Specification<Book> spec = (root, query, cb) -> cb.conjunction();

        if (title != null){
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (author != null){
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
        }

        if (minPrice != null && maxPrice != null && maxPrice > minPrice){
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("price"), minPrice, maxPrice));
        }

        return repository.findAll(spec)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<OpenLibraryBookDto> getAllInfoAboutBooks(String title, String author) {
        Specification<Book> spec = (root, query, cb) -> cb.conjunction();

        if (title != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (author != null) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
        }

        return repository.findAll(spec)
                .stream()
                .map(dto -> new OpenLibraryBookDto(
                        dto.getId(),
                        dto.getImageUrl(),
                        dto.getTitle(),
                        dto.getAuthor(),
                        dto.getYear(),
                        dto.getStock(),
                        dto.getPrice()
                ))
                .toList();
    }


    @Transactional
    public void deleteBookById(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Not found book by id " + id);
        }
        repository.deleteById(id);
    }
}
