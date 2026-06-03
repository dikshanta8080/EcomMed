package com.acharya.dikshanta.EcomMed.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductCreateResponse(
        UUID id,
        String name,
        BigDecimal price,
        String categoryName


) {
}
