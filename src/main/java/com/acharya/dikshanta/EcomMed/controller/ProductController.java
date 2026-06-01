package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.PageableRequest;
import com.acharya.dikshanta.EcomMed.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.EcomMed.dto.request.ProductSearchRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ProductResponse;
import com.acharya.dikshanta.EcomMed.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getProducts(
            @ModelAttribute PageableRequest pageableRequest,
            @ModelAttribute ProductSearchRequest searchRequest
    ) {
        PagedResponse<ProductResponse> allProducts = productService.findAllProducts(pageableRequest.toPageable(), searchRequest);
        return ResponseEntity.ok(ApiResponse.success(allProducts, "Product Fetched Successfully"));
    }
}
