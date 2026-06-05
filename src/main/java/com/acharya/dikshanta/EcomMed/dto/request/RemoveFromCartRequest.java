package com.acharya.dikshanta.EcomMed.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RemoveFromCartRequest(
        UUID productId,
        Integer quantity
) {
}
