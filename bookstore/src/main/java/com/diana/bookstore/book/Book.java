package com.diana.bookstore.book;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "year")
    private Integer year;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price")
    private BigDecimal price;

    public Book() {

    }

    public Book(Long id, String imageUrl, String title, String author,
                Integer year, Integer stock, BigDecimal price) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.author = author;
        this.year = year;
        this.stock = stock;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}