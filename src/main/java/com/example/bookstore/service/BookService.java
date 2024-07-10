package com.example.bookstore.service;

import com.example.bookstore.dto.UpdateBookDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional(rollbackOn= {SQLException.class})
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(rollbackOn= {SQLException.class})
    public Book updateBook(Long id,UpdateBookDTO book) {
        Optional<Book> oldbook = bookRepository.findById(id);
        if (oldbook.isPresent()) {
            Book book1 = oldbook.get();
            if (book.getTitle()!=null) {
                book1.setTitle(book.getTitle());
            }
            if (book.getDescription()!=null) {
                book1.setDescription(book.getDescription());
            }
            if (book.getGenre()!=null){
                book1.setGenre(book.getGenre());
            }
            if (book.getPrice()!=null){
                book1.setPrice(book.getPrice());
            }
            if (book.getIsbn()!=null){
                book1.setIsbn(book.getIsbn());
            }
            if (book.getPublicationDate()!=null){
                book1.setPublicationDate(book.getPublicationDate());
            }
            return bookRepository.save(book1);
        }else{
            return null;
        }
    }

    @Transactional(rollbackOn= {SQLException.class})
    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getBooksByPrice(BigDecimal priceMin,BigDecimal priceMax)  {
        return bookRepository.findBooksByPriceBetween(priceMin,priceMax);
    }

    public List<Book> getBooksByMinPrice(BigDecimal minPrice) {
        return bookRepository.findBooksByPriceGreaterThan(minPrice);
    }

    public List<Book> getBooksByMaxPrice(BigDecimal maxPrice) {
        return bookRepository.findBooksByPriceLessThan(maxPrice);
    }
}
