package com.example.bookstore.configuration;

import com.example.bookstore.Enum.OrderEvent;
import com.example.bookstore.Enum.OrderStatus;
import com.example.bookstore.exception.OrderProcessingException;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class OrderStateMachineListener extends StateMachineListenerAdapter<OrderStatus, OrderEvent> {

    @Override
    public void stateChanged(State<OrderStatus, OrderEvent> from, State<OrderStatus, OrderEvent> to) {
        System.out.println("State changed from " + (from != null ? from.getId() : "none") +
                " to " + (to != null ? to.getId() : "none"));
    }

    @Override
    public void stateMachineError(StateMachine<OrderStatus, OrderEvent> stateMachine, Exception exception) {
        System.err.println("State machine error occurred: " + exception.getMessage());
    }

    @Override
    public void eventNotAccepted(Message<OrderEvent> event) {
        throw new OrderProcessingException("Event not accepted: " + event.getPayload());
    }
}
