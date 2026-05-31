package com.acharya.dikshanta.EcomMed.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CategoryResponse(
        UUID id,
        String name,
        String description
) {
}
