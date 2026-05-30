package com.acharya.dikshanta.EcomMed.events;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserRegisteredEvent(
        UUID id,
        String name,
        String email

) {
}
