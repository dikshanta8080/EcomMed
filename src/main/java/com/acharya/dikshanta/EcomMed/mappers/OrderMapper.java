package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.dto.response.OrderResponse;
import com.acharya.dikshanta.EcomMed.model.Order;
import org.springframework.lang.NonNull;

import java.util.List;

public class OrderMapper {
    public static OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemsResponse> orderItemsResponse = getOrderItemsResponses(order);
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalPrice())
                .placedAt(order.getCreatedAt())
                .orderItemsResponses(orderItemsResponse)
                .build();
    }

    @NonNull
    private static List<OrderResponse.OrderItemsResponse> getOrderItemsResponses(Order order) {
        return order.getOrderItems()
                .stream()
                .map(orderItem -> OrderResponse.OrderItemsResponse.builder()
                        .quantity(orderItem.getQuantity())
                        .totalPrice(orderItem.getTotalPrice())
                        .productId(orderItem.getProduct().getId())
                        .productName(orderItem.getProduct().getName())
                        .unitPrice(orderItem.getPrice())
                        .build()).toList();
    }
}
