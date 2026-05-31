package com.acharya.dikshanta.EcomMed.repository;

import com.acharya.dikshanta.EcomMed.model.Inventory;
import com.acharya.dikshanta.EcomMed.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByProductId(UUID productId);

    boolean existsByProduct(Product product);

    boolean existsByProductId(UUID productId);

    Optional<Inventory> findByProduct(Product product);
}
