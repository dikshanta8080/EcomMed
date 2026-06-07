package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.dto.request.AddToCartRequest;
import com.acharya.dikshanta.EcomMed.dto.request.RemoveFromCartRequest;
import com.acharya.dikshanta.EcomMed.dto.response.CartResponse;
import com.acharya.dikshanta.EcomMed.dto.response.RemoveFromCartResponse;
import com.acharya.dikshanta.EcomMed.exceptions.BusinessException;
import com.acharya.dikshanta.EcomMed.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.EcomMed.mappers.CartResponseMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @PreAuthorize("hasAuthority('cart:add')")
    public CartResponse addToCart(AddToCartRequest request) {

        UUID userId = LoggedInUser.getLoggedInUser();

        validateStock(request);

        User user = getUser(userId);
        Product product = getProduct(request.productId());

        Cart cart = getOrCreateCart(user);

        CartItem cartItem = getOrCreateOrUpdateCartItem(cart, product, request.quantity());

        updateCartTotal(cart);

        log.info("Cart updated successfully: {}", cart.getId());
        return CartResponseMapper.toResponse(cart);

    }

    @Transactional
    public RemoveFromCartResponse removeFromCart(RemoveFromCartRequest request) {
        UUID loggedInUserId = LoggedInUser.getLoggedInUser();
        Cart cart = getCart(loggedInUserId);
        CartItem cartItem = findCartItem(cart, request);
        CartItem updatedItem = deleteOrReduceQuantity(cart, request, cartItem);
        cart.removeCartItem(updatedItem);
        cart.calculateTotal();
        Cart savedCart = cartRepository.save(cart);
        return toResponse(request, savedCart);
    }

    private RemoveFromCartResponse toResponse(RemoveFromCartRequest request, Cart cart) {
        return RemoveFromCartResponse.builder()
                .productId(request.productId())
                .cartId(cart.getId())
                .quantity(request.quantity())
                .build();
    }

    private CartItem findCartItem(Cart cart, RemoveFromCartRequest request) {
        return cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId()).orElseThrow(() ->
                new ResourceNotFoundException("Item not found"));
    }

    private CartItem deleteOrReduceQuantity(Cart cart, RemoveFromCartRequest request, CartItem cartItem
    ) {
        if (cartItem.getQuantity() <= request.quantity()) {
            cartItemRepository.delete(cartItem);

        } else {
            cartItem.setQuantity(cartItem.getQuantity() - request.quantity());
        }
        cartItem.calculateTotalPrice();
        return cartItem;

    }

    private Cart getCart(UUID userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    private void validateStock(AddToCartRequest request) {
        if (!inventoryService.checkAvailability(request.productId(), request.quantity())) {
            throw new BusinessException("The stock is not available");
        }
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
    }

    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));
    }

    private CartItem getOrCreateOrUpdateCartItem(Cart cart, Product product, int quantity) {

        return cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .map(existingItem -> {
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                    existingItem.calculateTotalPrice();
                    return cartItemRepository.save(existingItem);
                })
                .orElseGet(() -> {
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .product(product)
                            .unitPrice(product.getPrice())
                            .quantity(quantity)
                            .build();
                    cart.addCartItem(newItem);
                    newItem.calculateTotalPrice();
                    return cartItemRepository.save(newItem);
                });
    }

    private void updateCartTotal(Cart cart) {
        cart.calculateTotal();
        cart.calculateTotalItems();
        cart.calculateUniqueQuantity();
        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart() {
        UUID loggedInUser = LoggedInUser.getLoggedInUser();
        Cart cart = cartRepository.findByUserId(loggedInUser).orElseThrow(() ->
                new ResourceNotFoundException("cart not found"));
        return CartResponseMapper.toResponse(cart);
    }
}