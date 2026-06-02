package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.dto.request.AddToCartRequest;
import com.acharya.dikshanta.EcomMed.dto.response.AddToCartResponse;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.EcomMed.model.Cart;
import com.acharya.dikshanta.EcomMed.model.CartItem;
import com.acharya.dikshanta.EcomMed.model.Product;
import com.acharya.dikshanta.EcomMed.model.User;
import com.acharya.dikshanta.EcomMed.repository.CartItemRepository;
import com.acharya.dikshanta.EcomMed.repository.CartRepository;
import com.acharya.dikshanta.EcomMed.repository.ProductRepository;
import com.acharya.dikshanta.EcomMed.repository.UserRepository;
import com.acharya.dikshanta.EcomMed.utils.LoggedInUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @Transactional
//    @PreAuthorize("hasAuthority('CART_ADD')")
    public AddToCartResponse addToCart(AddToCartRequest request) {
        if (!inventoryService.checkAvailability(request.productId(), request.quantity())) {
            throw new BusinessException("The stock is not available");
        }
        UUID userId = LoggedInUser.getLoggedInUser();

        User loggedInUser = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User does not exists"));
        Product product = productRepository.findById(request.productId()).orElseThrow(() ->
                new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .user(loggedInUser)
                    .build();
            return cartRepository.save(newCart);

        });
        CartItem savedItem;
        Optional<CartItem> cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId());
        if (cartItem.isPresent()) {
            CartItem existingItem = cartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
            savedItem = cartItemRepository.save(existingItem);

        } else {
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .unitPrice(product.getPrice())
                    .quantity(request.quantity())
                    .cart(cart)
                    .build();
            savedItem = cartItemRepository.save(newItem);

        }
        savedItem.calculateTotalPrice();
        CartItem finalSavedItem = cartItemRepository.save(savedItem);
        cart.addCartItem(finalSavedItem);
        Cart savedCart = cartRepository.save(cart);
        savedCart.calculateTotal();
        Cart finalCart = cartRepository.save(savedCart);
        log.info("The cart is created {}", finalCart.getId());
        return AddToCartResponse.builder()
                .cartId(finalCart.getId())
                .quantity(finalSavedItem.getQuantity())
                .productId(finalSavedItem.getProduct().getId())
                .build();
    }

}
