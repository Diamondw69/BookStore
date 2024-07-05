package com.example.bookstore.repository;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByFirstName(String name);
    List<Author> findAuthorsByBooks(List<Book> books);
    List<Author> findAuthorsByLastName(String lastName);
}
