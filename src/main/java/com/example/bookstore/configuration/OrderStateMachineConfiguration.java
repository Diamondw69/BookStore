package com.example.bookstore.configuration;

import com.example.bookstore.Enum.OrderEvent;
import com.example.bookstore.Enum.OrderStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;


@Configuration
@EnableStateMachineFactory
public class OrderStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(final StateMachineStateConfigurer<OrderStatus,OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.NEW)
                .end(OrderStatus.DELIVERED)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<OrderStatus,OrderEvent> transition) throws Exception{
        transition
                .withExternal()
                    .source(OrderStatus.NEW)
                    .target(OrderStatus.CONFIRMED)
                    .event(OrderEvent.CONFIRM)
                    .and()
                .withExternal()
                    .source(OrderStatus.CONFIRMED)
                    .target(OrderStatus.PAID)
                    .event(OrderEvent.PAY)
                    .and()
                .withExternal()
                    .source(OrderStatus.PAID)
                    .target(OrderStatus.SHIPPED)
                    .event(OrderEvent.SHIP)
                    .and()
                .withExternal()
                    .source(OrderStatus.SHIPPED)
                    .target(OrderStatus.DELIVERED)
                    .event(OrderEvent.DELIVER)
                    .and()
                .withExternal()
                    .source(OrderStatus.NEW)
                    .target(OrderStatus.CANCELLED)
                    .event(OrderEvent.CANCEL)
                    .and()
                .withExternal()
                    .source(OrderStatus.CONFIRMED)
                    .target(OrderStatus.CANCELLED)
                    .event(OrderEvent.CANCEL)
                    .and()
                .withExternal()
                    .source(OrderStatus.PAID)
                    .target(OrderStatus.CANCELLED)
                    .event(OrderEvent.CANCEL)
                    .and()
                .withExternal()
                    .source(OrderStatus.SHIPPED)
                    .target(OrderStatus.CANCELLED)
                    .event(OrderEvent.CANCEL)
                    .and()
                .withExternal()
                    .source(OrderStatus.DELIVERED)
                    .target(OrderStatus.CANCELLED)
                    .event(OrderEvent.CANCEL);
    }


}
