package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.dto.response.OrderResponse;
import com.acharya.dikshanta.EcomMed.model.Order;

import java.util.List;

public class OrderMapper {
    public static OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemsResponse> orderItemsResponse = order.getOrderItems()
                .stream()
                .map(orderItem -> OrderResponse.OrderItemsResponse.builder()
                        .quantity(orderItem.getQuantity())
                        .totalPrice(orderItem.getTotalPrice())
                        .productId(orderItem.getProduct().getId())
                        .productName(orderItem.getProduct().getName())
                        .unitPrice(orderItem.getPrice())
                        .build()).toList();


        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalPrice())
                .placedAt(order.getCreatedAt())
                .orderItemsResponses(orderItemsResponse)
                .build();
    }
}
