package com.example.bookstore.dto;

import com.example.bookstore.entity.Book;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateAuthorDTO {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String biography;

    private List<Book> books;
}
