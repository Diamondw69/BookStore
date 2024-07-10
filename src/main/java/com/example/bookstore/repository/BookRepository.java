package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Book> findBooksByPriceGreaterThan(BigDecimal price);
    List<Book> findBooksByPriceLessThan(BigDecimal price);
}
