package com.acharya.dikshanta.EcomMed.events;

import com.acharya.dikshanta.EcomMed.enums.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderPlacedEvent(
        UUID orderId,
        OrderStatus orderStatus,
        String email,
        LocalDateTime placedAt,
        UUID userId,
        List<OrderItemEvent> orderItemEvents

) {
    @Builder
    public record OrderItemEvent(
            UUID productId,
            Integer quantity

    ) {

    }
}
