package com.example.bookstore.controller;


import com.example.bookstore.dto.UpdateBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice) {
        List<Book> books;

        if (minPrice != null && maxPrice != null) {
            books = bookService.getBooksByPrice(minPrice, maxPrice);
        } else if (minPrice != null) {
            books = bookService.getBooksByMinPrice(minPrice);
        } else if (maxPrice != null) {
            books = bookService.getBooksByMaxPrice(maxPrice);
        } else {
            books = bookService.getAllBooks();
        }

        return ResponseEntity.ok(books);
    }

    @PostMapping("/")
    ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book newBook = bookService.createBook(book);
        return ResponseEntity.ok(newBook);
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<Book>> getBookById(@PathVariable("id") long id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isEmpty()) {
            throw new ResourceNotFoundException("Book with id " + id + " not found");
        }
        return ResponseEntity.ok(book);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody UpdateBookDTO book) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
        Optional<Book> bookOptional = bookService.getBookById(id);
        if (bookOptional.isPresent()) {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Book not found");
        }
    }
}
