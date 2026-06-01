package com.acharya.dikshanta.EcomMed.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductSearchRequest(
        String name,
        BigDecimal minPrice,
        BigDecimal maxPrice

) {
}
