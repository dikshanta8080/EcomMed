package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.PageableRequest;
import com.acharya.dikshanta.EcomMed.dto.request.ProductCreateRequest;
import com.acharya.dikshanta.EcomMed.dto.request.ProductSearchRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ProductCreateResponse;
import com.acharya.dikshanta.EcomMed.dto.response.ProductResponse;
import com.acharya.dikshanta.EcomMed.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Create a new product with an image")
    @RequestBody(
        content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            schema = @Schema(type = "object"),
            encoding = {
                @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE),
                @Encoding(name = "file", contentType = MediaType.IMAGE_PNG_VALUE)
            }
        )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestPart(name = "data") ProductCreateRequest request,
            @RequestPart(name = "file") MultipartFile file
    ) {
        ProductCreateResponse product = productService.createProduct(request, file);
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
