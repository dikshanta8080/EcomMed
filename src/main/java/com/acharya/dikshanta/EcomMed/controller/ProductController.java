package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ProductResponse;
import com.acharya.dikshanta.EcomMed.service.ProductService;
import com.acharya.dikshanta.EcomMed.utils.constrants.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductCreateRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(product, MessageConstants.ProductConstants.PRODUCT_CREATED));
    }
}
