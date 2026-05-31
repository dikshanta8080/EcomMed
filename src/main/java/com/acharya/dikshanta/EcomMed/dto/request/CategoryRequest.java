package com.acharya.dikshanta.EcomMed.dto.request;

import lombok.Builder;

@Builder
public record CategoryRequest(
        String name,
        String description
) {
}
