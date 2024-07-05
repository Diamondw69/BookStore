package com.example.bookstore.controller;


import com.example.bookstore.dto.UpdateBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {


    BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }


    @GetMapping("/")
    ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false)BigDecimal MinPrice,
                                           @RequestParam(required = false)BigDecimal MaxPrice){
        List<Book> books = bookService.getAllBooks();
        if (books==null || books.isEmpty()){
            throw new ResourceNotFoundException("Books not found");
        }
        if (MinPrice != null && MaxPrice != null){
            List<Book> filteredBooks = bookService.getBooksByPrice(MinPrice,MaxPrice);
            if (!filteredBooks.isEmpty()){
                return ResponseEntity.ok(filteredBooks);
            }else{
                throw new ResourceNotFoundException("Books not found");
            }
        }
        if (MinPrice != null){
            List<Book> filteredBooks = bookService.getBooksByMinPrice(MinPrice);
            if (!filteredBooks.isEmpty()){
                return ResponseEntity.ok(filteredBooks);
            }else{
                throw new ResourceNotFoundException("Books not found");
            }

        }
        if (MaxPrice != null){
            List<Book> filteredBooks = bookService.getBooksByMaxPrice(MaxPrice);
            if (!filteredBooks.isEmpty()){
                return ResponseEntity.ok(filteredBooks);
            }else{
                throw new ResourceNotFoundException("Books not found");
            }
        }
        return ResponseEntity.ok(books);
    }

    @PostMapping("/")
    ResponseEntity<Book> createBook(@Valid @RequestBody Book book){
        Book newBook = bookService.createBook(book);
        return ResponseEntity.ok(newBook);
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<Book>> getBookById(@PathVariable("id") long id){
        Optional<Book> book = bookService.getBookById(id);
        if (book.isEmpty()){
            throw new ResourceNotFoundException("Book with id " + id + " not found");
        }
        return ResponseEntity.ok(book);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Book> updateBook(@PathVariable("id") long id,@RequestBody UpdateBookDTO book){
        Optional<Book> bookOptional = bookService.getBookById(id);
        if(bookOptional.isPresent()){
            Book updatedBook = bookService.updateBook(id,book);
            return ResponseEntity.ok(updatedBook);
        }else{
            throw new ResourceNotFoundException("Book not found");
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteBook(@PathVariable("id") long id){
        Optional<Book> bookOptional = bookService.getBookById(id);
        if(bookOptional.isPresent()){
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        }else{
            throw new ResourceNotFoundException("Book not found");
        }

    }
}
