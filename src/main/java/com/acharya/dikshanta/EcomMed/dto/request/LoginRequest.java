package com.acharya.dikshanta.EcomMed.dto.request;

import com.acharya.dikshanta.EcomMed.validators.EmailValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @EmailValidator(message = "Please provide valid email") String email,
        @NotBlank(message = "Please provide valid password") String password
) {
}
