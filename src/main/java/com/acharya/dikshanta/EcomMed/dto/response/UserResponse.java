package com.acharya.dikshanta.EcomMed.dto.response;

import com.acharya.dikshanta.EcomMed.enums.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String name,
        Role role,
        String email
) {
}
