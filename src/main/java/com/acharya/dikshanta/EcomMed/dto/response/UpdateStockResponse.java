package com.acharya.dikshanta.EcomMed.dto.response;

import lombok.Builder;

@Builder
public record UpdateStockResponse(
        String name,
        Integer availableQuantity
) {
}
