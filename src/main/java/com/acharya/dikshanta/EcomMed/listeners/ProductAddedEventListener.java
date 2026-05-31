package com.acharya.dikshanta.EcomMed.listeners;

import com.acharya.dikshanta.EcomMed.events.ProductCreatedEvent;
import com.acharya.dikshanta.EcomMed.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductAddedEventListener {
    private final InventoryService inventoryService;

    @EventListener
    public void handleAddProductEvent(ProductCreatedEvent event) {
        inventoryService.createInventory(event);
    }
}
