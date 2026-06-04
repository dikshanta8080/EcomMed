package com.acharya.dikshanta.EcomMed.dto.response;

import com.acharya.dikshanta.EcomMed.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID orderId,
        OrderStatus orderStatus,
        LocalDateTime placedAt,
        UUID userId,
        BigDecimal totalAmount,
        List<OrderItemsResponse> orderItemsResponses

) {
    @Builder
    public record OrderItemsResponse(
            UUID productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice
    ) {

    }
}
