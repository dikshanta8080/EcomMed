package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.dto.request.AddToCartRequest;
import com.acharya.dikshanta.EcomMed.dto.response.AddToCartResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddToCartResponse>> addToCart(@RequestBody @Valid AddToCartRequest request) {
        AddToCartResponse addToCartResponse = cartService.addToCart(request);
        return ResponseEntity.ok(ApiResponse.success(addToCartResponse, "Successfully Added to Cart"));
    }
}
