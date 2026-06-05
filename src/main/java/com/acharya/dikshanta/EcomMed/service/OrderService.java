package com.acharya.dikshanta.EcomMed.service;

import com.acharya.dikshanta.EcomMed.dto.response.OrderResponse;
import com.acharya.dikshanta.EcomMed.enums.OrderStatus;
import com.acharya.dikshanta.EcomMed.exceptions.ResourceNotFoundException;
import com.acharya.dikshanta.EcomMed.mappers.OrderMapper;
import com.acharya.dikshanta.EcomMed.model.Cart;
import com.acharya.dikshanta.EcomMed.model.Order;
import com.acharya.dikshanta.EcomMed.model.OrderItem;
import com.acharya.dikshanta.EcomMed.model.User;
import com.acharya.dikshanta.EcomMed.repository.CartRepository;
import com.acharya.dikshanta.EcomMed.repository.OrderRepository;
import com.acharya.dikshanta.EcomMed.repository.UserRepository;
import com.acharya.dikshanta.EcomMed.utils.LoggedInUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final InventoryService inventoryService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @PreAuthorize("hasAuthority('order:place')")
    public OrderResponse placeOrder() {

        UUID userId = LoggedInUser.getLoggedInUser();
        User user = getUser(userId);
        Cart cart = getCart(userId);
        Order order = createOrder(user);
        addOrderItemsFromCart(cart, order);
        order.calculateTotal();
        Order savedOrder = orderRepository.save(order);
        clearCart(cart);

        return OrderMapper.toResponse(savedOrder);
    }


    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Cart getCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    private Order createOrder(User user) {
        return Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .build();
    }

    private void addOrderItemsFromCart(Cart cart, Order order) {

        cart.getCartItems().forEach(cartItem -> {

            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getUnitPrice())
                    .build();

            orderItem.calculateTotal();
            order.addOrderItem(orderItem);
        });
    }

    private void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cartRepository.delete(cart);
    }
}