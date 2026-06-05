package com.acharya.dikshanta.EcomMed.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RemoveFromCartResponse(
        UUID cartId,
        UUID productId,
        Integer quantity
) {
}
