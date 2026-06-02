package com.acharya.dikshanta.EcomMed.repository;

import com.acharya.dikshanta.EcomMed.model.Cart;
import com.acharya.dikshanta.EcomMed.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID id);

    Optional<Cart> findByUser(User user);
}
