package com.example.bookstore.repository;

import com.example.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAuthorsByLastName(String lastName);
    List<Author> findAuthorsByFirstName(String firstName);
    List<Author> findAuthorsByFirstNameAndLastName(String firstName, String lastName);
}
