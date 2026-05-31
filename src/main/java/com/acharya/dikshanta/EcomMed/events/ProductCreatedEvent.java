package com.acharya.dikshanta.EcomMed.events;

import com.acharya.dikshanta.EcomMed.model.Product;
import lombok.Builder;

@Builder
public record ProductCreatedEvent(
        Product product,
        Integer quantity

) {
}
