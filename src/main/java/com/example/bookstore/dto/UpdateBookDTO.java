package com.example.bookstore.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateBookDTO {

    private String title;

    private String isbn;

    private LocalDate publicationDate;

    private BigDecimal price;

    private String genre;

    private String description;
}
