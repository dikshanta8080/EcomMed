package com.acharya.dikshanta.EcomMed.mappers;

import com.acharya.dikshanta.EcomMed.dto.response.CartResponse;
import com.acharya.dikshanta.EcomMed.model.Cart;

import java.util.List;

public class CartResponseMapper {
    public static CartResponse toResponse(Cart cart) {
        List<CartResponse.CartItemResponse> cartItemResponseStream = cart.getCartItems().stream().map((cartItem) -> CartResponse.CartItemResponse
                .builder()
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .itemTotal(cartItem.getTotalPrice())
                .build()

        ).toList();

        return CartResponse.builder()
                .cartId(cart.getId())
                .cartTotal(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .items(cartItemResponseStream)
                .build();
    }
}
