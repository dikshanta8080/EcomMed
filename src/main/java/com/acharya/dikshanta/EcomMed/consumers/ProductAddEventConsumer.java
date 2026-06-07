package com.acharya.dikshanta.EcomMed.consumers;

import com.acharya.dikshanta.EcomMed.constrants.KafkaTopics;
import com.acharya.dikshanta.EcomMed.events.ProductCreatedEvent;
import com.acharya.dikshanta.EcomMed.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductAddEventConsumer {
    private final InventoryService inventoryService;

    @KafkaListener(topics = KafkaTopics.PRODUCT_ADDED, groupId = "decrease-stock")
    public void decreaseStock(ProductCreatedEvent event) {
        inventoryService.createInventory(event);
    }
}
