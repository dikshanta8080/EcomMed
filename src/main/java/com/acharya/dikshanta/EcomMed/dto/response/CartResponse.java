package com.acharya.dikshanta.EcomMed.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CartResponse(
        UUID cartId,
        Integer totalItems,
        BigDecimal cartTotal,
        List<CartItemResponse> items
) {

    @Builder
    public record CartItemResponse(
            UUID productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal itemTotal
    ) {
    }
}
