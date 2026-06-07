package com.acharya.dikshanta.EcomMed.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record OrderFilterRequest(
        LocalDate startDate,
        LocalDate endDate
) {
}
