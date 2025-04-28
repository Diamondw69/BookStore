package com.example.bookstore.controller;

import com.example.bookstore.Enum.OrderEvent;
import com.example.bookstore.Enum.OrderStatus;
import com.example.bookstore.dto.UpdateOrderDTO;
import com.example.bookstore.entity.Order;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order API", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Get all orders",
            description = "Retrieves all orders from the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Orders not found")
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Orders not found");
        }
        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order and sets the initial order date and status.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid order input")
            }
    )
    @PostMapping("/")
    public ResponseEntity<Order> create(@Valid @RequestBody Order order) {
        LocalDate date = LocalDate.now();
        order.setOrderDate(date);
        order.setStatus(OrderStatus.NEW);
        Order newOrder = orderService.createOrder(order);
        return ResponseEntity.ok(newOrder);
    }

    @Operation(
            summary = "Get order by ID",
            description = "Retrieves a specific order by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Order>> getById(
            @Parameter(description = "ID of the order to retrieve", required = true, in = ParameterIn.PATH)
            @PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isEmpty()) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
        return ResponseEntity.ok(order);
    }

    @Operation(
            summary = "Update an order",
            description = "Updates the details of an existing order.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Order> update(
            @Parameter(description = "ID of the order to update", required = true, in = ParameterIn.PATH)
            @PathVariable Long id,
            @RequestBody UpdateOrderDTO order) {
        Optional<Order> oldOrder = orderService.getOrderById(id);
        if (oldOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @Operation(
            summary = "Delete an order",
            description = "Deletes an existing order by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID of the order to delete", required = true, in = ParameterIn.PATH)
            @PathVariable Long id) {
        Optional<Order> oldOrder = orderService.getOrderById(id);
        if (oldOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order with id " + id + " not found");
        }
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update order status",
            description = "Updates the status of an order based on an order event.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @Parameter(description = "ID of the order to update", required = true, in = ParameterIn.PATH)
            @PathVariable Long orderId,
            @Parameter(description = "Order event to update the status", required = true, in = ParameterIn.QUERY)
            @RequestParam OrderEvent event) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, event));
    }
}
