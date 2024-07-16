package com.example.bookstore.controller;


import com.example.bookstore.Enum.OrderEvent;
import com.example.bookstore.Enum.OrderStatus;
import com.example.bookstore.dto.UpdateOrderDTO;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Order;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/")
    ResponseEntity<List<Order>> getAll() {
        List<Order> orders;

        orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Orders not found");
        }

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/")
    ResponseEntity<Order> create(@Valid @RequestBody Order order) {
        LocalDate date = LocalDate.now();
        order.setOrderDate(date);
        order.setStatus(OrderStatus.NEW);
        Order newOrder = orderService.createOrder(order);
        return ResponseEntity.ok(newOrder);
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<Order>> getById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Order> update(@PathVariable Long id, @RequestBody UpdateOrderDTO order) {
        Optional<Order> oldOrder = orderService.getOrderById(id);
        if (oldOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Order> oldOrder = orderService.getOrderById(id);
        if (oldOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderEvent event) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, event));
    }

}
