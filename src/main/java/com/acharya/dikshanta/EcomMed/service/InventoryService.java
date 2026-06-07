package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.response.UpdateStockResponse;
import com.acharya.dikshanta.EcomMed.events.OrderPlacedEvent;
import com.acharya.dikshanta.EcomMed.events.ProductCreatedEvent;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.EcomMed.model.Inventory;
import com.acharya.dikshanta.EcomMed.model.Product;
import com.acharya.dikshanta.EcomMed.repository.InventoryRepository;
import com.acharya.dikshanta.EcomMed.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final EntityManager entityManager;
    private final ProductRepository productRepository;

    @Transactional(readOnly = false)
    public void createInventory(ProductCreatedEvent event) {
        Product product = productRepository.findById(event.productId()).orElseThrow(() ->
                new ResourceNotFoundException("product not found"));
        Inventory inventory = inventoryRepository.findByProductId(event.productId()).orElseGet(() ->
                Inventory.builder()
                        .quantity(event.quantity())
                        .product(product)
                        .name(product.getName())
                        .build());

        inventoryRepository.save(inventory);

    }

    @Transactional
    public Inventory findInventory(UUID productId) {
        return inventoryRepository.findByProductId(productId).orElseThrow(() ->
                new ResourceNotFoundException(MessageConstants.InventoryConstants.INVENTORY_NOT_FOUND));

    }

    @Transactional
    public UpdateStockResponse updateStock(UUID productId, Integer quantity) {
        validateQuantity(quantity);
        Inventory inventory = findInventory(productId);
        inventory.setQuantity(inventory.getQuantity() + quantity);
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
    public UpdateStockResponse decreaseStock(UUID productId, Integer quantity) {
        validateQuantity(quantity);
        Inventory inventory = findInventory(productId);
        if (inventory.getQuantity() < quantity) {
            throw new BusinessException("Can not decrease more than the available one");
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return UpdateStockResponse.builder()
                .name(savedInventory.getName())
                .availableQuantity(savedInventory.getQuantity())
                .build();
    }

    @Transactional
    public void decreaseInventoryStock(List<OrderPlacedEvent.OrderItemEvent> itemEventsList) {
        itemEventsList.forEach(itemEvents -> decreaseStock(itemEvents.productId(), itemEvents.quantity()));
    }


    public boolean checkAvailability(UUID productId, Integer quantity) {
        Inventory inventory = findInventory(productId);
        return inventory.getQuantity() >= quantity;
    }

}
