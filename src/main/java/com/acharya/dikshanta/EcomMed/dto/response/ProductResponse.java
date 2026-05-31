package com.acharya.dikshanta.EcomMed.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID id,
        String name,
        BigDecimal price,
        Integer quantity,
        UUID categoryId
) {
}
