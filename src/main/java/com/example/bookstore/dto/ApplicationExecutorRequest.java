package com.example.bookstore.dto;

import com.example.bookstore.Enum.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationExecutorRequest {

    private Long bookId;
    private Long orderId;
    private Long executorId;
    private String description;
    private OrderEvent event;
}
