package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.model.Order;

import java.util.List;

public class OrderEventMapper {
    public static OrderPlacedEvent toOrderEvent(Order order) {
        List<OrderPlacedEvent.OrderItemEvent> orderItemEvents = order.getOrderItems().stream().map(orderItem ->
                OrderPlacedEvent.OrderItemEvent.builder()
                        .quantity(orderItem.getQuantity())
                        .productId(orderItem.getProduct().getId())
                        .build()).toList();
        return OrderPlacedEvent.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .placedAt(order.getCreatedAt())
                .email(order.getUser().getEmail())
                .userId(order.getUser().getId())
                .orderItemEvents(orderItemEvents)
                .build();
    }
}
