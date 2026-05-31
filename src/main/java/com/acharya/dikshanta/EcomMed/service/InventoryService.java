package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.events.ProductCreatedEvent;
import com.acharya.dikshanta.EcomMed.model.Inventory;
import com.acharya.dikshanta.EcomMed.repository.InventoryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = false)
    public void createInventory(ProductCreatedEvent event) {
//        Product product = entityManager.getReference(Product.class, event.productId());

        Inventory inventory = inventoryRepository.findByProductId(event.product().getId()).orElseGet(() ->
                Inventory.builder().quantity(event.quantity()).product(event.product()).name(event.product().getName()).build());

        inventoryRepository.save(inventory);

    }


}
