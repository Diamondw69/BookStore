package com.example.bookstore.dto;

import com.example.bookstore.Enum.OrderStatus;
import com.example.bookstore.entity.Book;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateOrderDTO {

    private Book book;

    private String customerName;

    private OrderStatus status;

    private LocalDate orderDate;
}
