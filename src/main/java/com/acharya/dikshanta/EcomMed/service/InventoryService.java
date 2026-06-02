package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.UpdateStockRequest;
import com.acharya.dikshanta.EcomMed.dto.response.UpdateStockResponse;
import com.acharya.dikshanta.EcomMed.events.ProductCreatedEvent;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.EcomMed.model.Inventory;
import com.acharya.dikshanta.EcomMed.repository.InventoryRepository;
import com.acharya.dikshanta.EcomMed.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final EntityManager entityManager;
    private final ProductRepository productRepository;

    @Transactional(readOnly = false)
    public void createInventory(ProductCreatedEvent event) {
//        Product product = entityManager.getReference(Product.class, event.productId());

        Inventory inventory = inventoryRepository.findByProductId(event.product().getId()).orElseGet(() ->
                Inventory.builder()
                        .quantity(event.quantity())
                        .product(event.product())
                        .name(event.product().getName())
                        .build());

        inventoryRepository.save(inventory);

    }

    @Transactional
    public Inventory findInventory(UUID productId) {
        return inventoryRepository.findByProductId(productId).orElseThrow(() ->
                new ResourceNotFoundException(MessageConstants.InventoryConstants.INVENTORY_NOT_FOUND));

    }

    @Transactional
    public UpdateStockResponse updateStock(UpdateStockRequest request) {
        validateQuantity(request.quantity());
        Inventory inventory = findInventory(request.productId());
        inventory.setQuantity(inventory.getQuantity() + request.quantity());
        Inventory savedInventory = inventoryRepository.save(inventory);
        return UpdateStockResponse.builder()
                .name(inventory.getName())
                .availableQuantity(savedInventory.getQuantity())
                .build();
    }


    private void validateQuantity(Integer quantity) {
        if (quantity < 1) throw new BusinessException(MessageConstants.InventoryConstants.INVALID_QUANTITY);
    }

    @Transactional
    public UpdateStockResponse decreaseStock(UpdateStockRequest request) {
        validateQuantity(request.quantity());
        Inventory inventory = findInventory(request.productId());
        if (inventory.getQuantity() < request.quantity()) {
            throw new BusinessException("Can not decrease more than the available one");
        }
        inventory.setQuantity(inventory.getQuantity() - request.quantity());
        Inventory savedInventory = inventoryRepository.save(inventory);
        return UpdateStockResponse.builder()
                .name(savedInventory.getName())
                .availableQuantity(savedInventory.getQuantity())
                .build();
    }


    public boolean checkAvailability(UUID productId, Integer quantity) {
        Inventory inventory = findInventory(productId);
        return inventory.getQuantity() >= quantity;
    }

}
