package com.example.bookstore.service;

import com.example.bookstore.Enum.OrderEvent;
import com.example.bookstore.Enum.OrderStatus;
import com.example.bookstore.dto.ApplicationExecutorRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Order;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class StateMachineProcessService {

    private final StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;
    private final BookRepository certificateApplicationRepository;
    private final OrderRepository orderRepository;

    public StateMachineProcessService(StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory, BookRepository certificateApplicationRepository, OrderRepository orderRepository) {
        this.stateMachineFactory = stateMachineFactory;
        this.certificateApplicationRepository = certificateApplicationRepository;
        this.orderRepository = orderRepository;
    }

    public void onPayment(StateContext<OrderStatus, OrderEvent> context) {
        Long applicationId = (Long) context.getExtendedState().getVariables().get("bookId");
        Book application = certificateApplicationRepository.findById(applicationId).orElseThrow(() ->
                new RuntimeException("Application not found"));
        application.setQuantity(application.getQuantity() - 1);
        certificateApplicationRepository.saveAndFlush(application);
    }

    public void onCancel(StateContext<OrderStatus, OrderEvent> context) {
        Long applicationId = (Long) context.getExtendedState().getVariables().get("bookId");
        Book application = certificateApplicationRepository.findById(applicationId).orElseThrow(() ->
                new RuntimeException("Application not found"));
        application.setQuantity(application.getQuantity() + 1);
        certificateApplicationRepository.saveAndFlush(application);
    }
    
    public void processApplication(ApplicationExecutorRequest applicationExecutorRequest) {
        try {
            StateMachine<OrderStatus, OrderEvent> stateMachine = build(applicationExecutorRequest.getBookId(),applicationExecutorRequest.getOrderId());
            stateMachine.getExtendedState().getVariables().put("bookId", applicationExecutorRequest.getBookId());
            stateMachine.getExtendedState().getVariables().put("orderId", applicationExecutorRequest.getOrderId());
            stateMachine.getExtendedState().getVariables().put("request", applicationExecutorRequest);
            log.info("Starting state machine processing");

            stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(applicationExecutorRequest.getEvent())
                            .setHeader("bookId", applicationExecutorRequest.getBookId())
                            .build()))
                    .doOnComplete(() -> {
                        OrderStatus newState = stateMachine.getState().getId();
                        log.info("State changed successfully: {}", newState);
                    })
                    .doOnError(e -> log.error("Error during state change", e))
                    .subscribe();

            logApplicationState(stateMachine, applicationExecutorRequest);

        } catch (Exception e) {
            log.error("Error processing application", e);
            throw new RuntimeException(e);
        }
    }

    private void logApplicationState(StateMachine<OrderStatus, OrderEvent> stateMachine, ApplicationExecutorRequest applicationExecutorRequest) {
        Long applicationId = (Long) stateMachine.getExtendedState().getVariables().get("bookId");
        if (applicationId == null) {
            log.error("Application ID is missing in the context");
            return;
        }
        log.info("Application ID: {}", applicationId);

        ApplicationExecutorRequest request = (ApplicationExecutorRequest) stateMachine.getExtendedState().getVariables().get("request");
        if (request == null) {
            log.error("Request is missing in the context");
            return;
        }
        log.info("Request: {}", request.toString());

        OrderStatus currentState = stateMachine.getState().getId();
        log.info("Current state before transition: {}", currentState);
        log.info("Event: {}", applicationExecutorRequest.getEvent());

        OrderStatus afterState = stateMachine.getState().getId();
        log.info("Current state after transition: {}", afterState);
    }

    private StateMachine<OrderStatus, OrderEvent> build(Long applicationId,Long orderId) {
        Book procurementApplication = certificateApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        log.info("Building state machine for application ID: {}", applicationId);

        StateMachine<OrderStatus, OrderEvent> stateMachine = stateMachineFactory.getStateMachine("1");
        stateMachine.stopReactively().block();

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<>() {
                        @Override
                        public void preStateChange(State<OrderStatus, OrderEvent> state,
                                                   Message<OrderEvent> message, Transition<OrderStatus,
                                OrderEvent> transition, StateMachine<OrderStatus, OrderEvent> stateMachine,
                                                   StateMachine<OrderStatus, OrderEvent> rootStateMachine) {
                            Optional.ofNullable(message).ifPresent(msg -> {
                                Long applicationId = (Long) msg.getHeaders().getOrDefault("bookId", -1L);
                                Book application = certificateApplicationRepository.findById(applicationId)
                                        .orElseThrow(() -> new IllegalArgumentException("Application not found"));
                                Order order = orderRepository.findById(applicationId)
                                        .orElseThrow(() -> new IllegalArgumentException("Application not found"));
                                order.setStatus(state.getId());
                                certificateApplicationRepository.save(application);
                                orderRepository.save(order);
                                log.info("Saved new status: {} for application ID: {}", state.getId(), applicationId);
                            });
                        }
                    });
                    sma.resetStateMachineReactively(new DefaultStateMachineContext<>(
                            order.getStatus(), null, null, null
                    )).block();
                });

        stateMachine.startReactively().block();
        return stateMachine;
    }
}
