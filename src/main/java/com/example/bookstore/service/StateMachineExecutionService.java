package com.example.bookstore.service;

import com.example.bookstore.Enum.OrderEvent;
import com.example.bookstore.Enum.OrderStatus;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Order;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StateMachineExecutionService {

    private final BookRepository certificateApplicationRepository;
    private final OrderRepository orderRepository;

    public void onPayment(StateContext<OrderStatus, OrderEvent> context) {
        Long applicationId = (Long) context.getExtendedState().getVariables().get("bookId");
        Book application = certificateApplicationRepository.findById(applicationId).orElseThrow(() ->
                new RuntimeException("Application not found"));
        Order order = orderRepository.findById(application.getId()).orElseThrow(() ->
                new RuntimeException("Application not found"));
        application.setQuantity(application.getQuantity() - order.getQuantity());
        certificateApplicationRepository.saveAndFlush(application);
    }

    public void onCancel(StateContext<OrderStatus, OrderEvent> context) {
        Long applicationId = (Long) context.getExtendedState().getVariables().get("bookId");
        Book application = certificateApplicationRepository.findById(applicationId).orElseThrow(() ->
                new RuntimeException("Application not found"));
        Order order = orderRepository.findById(application.getId()).orElseThrow(() ->
                new RuntimeException("Application not found"));
        application.setQuantity(application.getQuantity() + order.getQuantity());
        certificateApplicationRepository.saveAndFlush(application);
    }
}
