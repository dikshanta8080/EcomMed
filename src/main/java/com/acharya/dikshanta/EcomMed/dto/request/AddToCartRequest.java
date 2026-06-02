package com.acharya.dikshanta.EcomMed.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AddToCartRequest(
        UUID productId,
        Integer quantity
) {
}
