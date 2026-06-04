package com.acharya.dikshanta.EcomMed.events;

import com.acharya.dikshanta.EcomMed.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderPlacedEvent(
        UUID orderId,
        OrderStatus orderStatus,
        LocalDateTime placedAt,
        UUID userId,
        BigDecimal totalAmount
) {
    @Builder
    record OrderItemEvent(
            UUID productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice
    ) {

    }
}
