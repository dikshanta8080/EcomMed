package com.acharya.dikshanta.EcomMed.repository;

import com.acharya.dikshanta.EcomMed.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

    Optional<CartItem> findByCartId(UUID id);

    Optional<CartItem> findByProductId(UUID id);
}
