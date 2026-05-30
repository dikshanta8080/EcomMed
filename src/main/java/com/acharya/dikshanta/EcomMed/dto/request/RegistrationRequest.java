package com.acharya.dikshanta.EcomMed.dto.request;

import com.acharya.dikshanta.EcomMed.validators.EmailValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegistrationRequest(
        @NotBlank(message = "Please provide valid name") String name,
        @EmailValidator @NotBlank(message = "Please provide valid email") String email,
        @NotBlank(message = "Please provide valid password") String password
) {
}
