package com.acharya.dikshanta.EcomMed.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductCreatedEvent(
        UUID productId,
        Integer quantity

) {
}
