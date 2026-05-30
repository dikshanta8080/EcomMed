package com.acharya.dikshanta.EcomMed.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record LoginResponse(
        UUID id,
        String token,
        String name,
        String email

) {
}
