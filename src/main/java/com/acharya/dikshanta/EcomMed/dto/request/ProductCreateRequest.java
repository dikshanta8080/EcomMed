package com.acharya.dikshanta.EcomMed.dto.request;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductCreateRequest(
        String name,
        BigDecimal price,
        Integer quantity,
        UUID CategoryId

) {
}
