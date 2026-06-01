package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.constrants.MessageConstants;
import com.acharya.dikshanta.EcomMed.dto.request.CategoryRequest;
import com.acharya.dikshanta.EcomMed.dto.request.PageableRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.CategoryResponse;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CategoryResponse>>> getCategories(
            @ModelAttribute PageableRequest pageableRequest
    ) {
        PagedResponse<CategoryResponse> allCategories = categoryService.findAllCategories(pageableRequest.toPageable());
        return ResponseEntity.ok(ApiResponse.success(allCategories, MessageConstants.CategoryConstants.CATEGORY_FETCHED));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> addCategory(@RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(category, MessageConstants.CategoryConstants.CATEGORY_ADDED));
    }

}
