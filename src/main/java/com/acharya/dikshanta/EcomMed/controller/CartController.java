package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.dto.request.AddToCartRequest;
import com.acharya.dikshanta.EcomMed.dto.request.RemoveFromCartRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.CartResponse;
import com.acharya.dikshanta.EcomMed.dto.response.RemoveFromCartResponse;
import com.acharya.dikshanta.EcomMed.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@RequestBody @Valid AddToCartRequest request) {
        CartResponse cartResponse = cartService.addToCart(request);
        return ResponseEntity.ok(ApiResponse.success(cartResponse, "Successfully Added to Cart"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<RemoveFromCartResponse>> removeFromCart(@Valid RemoveFromCartRequest request) {
        RemoveFromCartResponse removeFromCartResponse = cartService.removeFromCart(request);
        return ResponseEntity.ok(ApiResponse.success(removeFromCartResponse, "Item removed from cart"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart(), "Cart fetched"));
    }
}
