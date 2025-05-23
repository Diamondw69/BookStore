package com.example.bookstore.service;


import com.example.bookstore.Enum.OrderEvent;
import com.example.bookstore.Enum.OrderStatus;
import com.example.bookstore.configuration.OrderStateMachineListener;
import com.example.bookstore.dto.UpdateOrderDTO;
import com.example.bookstore.entity.Order;
import com.example.bookstore.exception.OrderProcessingException;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.ReactiveStateMachineAccess;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final StateMachineFactory<OrderStatus,OrderEvent> stateMachineFactory;

    public List<Order> getAllOrders() {
        return orderRepository.findAllWithDeliveredLast();
    }

    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderById(userId);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional(rollbackOn = {SQLException.class})
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Transactional(rollbackOn = {SQLException.class})
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional(rollbackOn = {SQLException.class})
    public Order updateOrder(Long id, UpdateOrderDTO updateOrderDTO) {
        Optional<Order> old = orderRepository.findById(id);
        if (old.isPresent()) {
            Order oldOrder = old.get();
            if (updateOrderDTO.getBook() != null) {
                oldOrder.setBook(updateOrderDTO.getBook());
            }
            if (updateOrderDTO.getStatus() != null) {
                oldOrder.setStatus(updateOrderDTO.getStatus());
            }
            if (updateOrderDTO.getOrderDate() != null) {
                oldOrder.setOrderDate(updateOrderDTO.getOrderDate());
            }
            if (updateOrderDTO.getCustomerName() != null) {
                oldOrder.setCustomerName(updateOrderDTO.getCustomerName());
            }
            return orderRepository.save(oldOrder);
        } else {
            return null;
        }
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderEvent event) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        StateMachine<OrderStatus, OrderEvent> sm = build(order);

        sm.sendEvent(Mono.just(MessageBuilder.withPayload(event).build())).subscribe();

        if (order.getStatus() == sm.getState().getId()) {
            throw new IllegalStateException("Incorrect order status");
        }
        order.setStatus(sm.getState().getId());

        return orderRepository.save(order);

    }

    private StateMachine<OrderStatus, OrderEvent> build(Order order) {
        StateMachine<OrderStatus, OrderEvent> sm = stateMachineFactory.getStateMachine(order.getId().toString());
        sm.stopReactively().subscribe();

        sm.addStateListener(new OrderStateMachineListener());

        for (ReactiveStateMachineAccess<OrderStatus, OrderEvent> sma : sm.getStateMachineAccessor().withAllRegions()) {
            sma.resetStateMachineReactively(new DefaultStateMachineContext<>(order.getStatus(), null, null, null)).subscribe();
        }

        sm.startReactively().subscribe();
        return sm;
    }
}
