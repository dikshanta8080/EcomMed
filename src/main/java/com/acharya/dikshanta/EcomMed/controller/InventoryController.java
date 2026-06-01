package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.dto.request.UpdateStockRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.UpdateStockResponse;
import com.acharya.dikshanta.EcomMed.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    @PutMapping("/update-stock")
    public ResponseEntity<ApiResponse<UpdateStockResponse>> updateStock(@RequestBody UpdateStockRequest request) {
        UpdateStockResponse updateStockResponse = inventoryService.updateStock(request);
        return ResponseEntity.ok(ApiResponse.success(updateStockResponse, "Stocked updates successfully"));
    }

    @DeleteMapping("/delete-stock")
    public ResponseEntity<ApiResponse<UpdateStockResponse>> deleteStock(@RequestBody UpdateStockRequest request) {
        UpdateStockResponse updateStockResponse = inventoryService.decreaseStock(request);
        return ResponseEntity.ok(ApiResponse.success(updateStockResponse, "Stocked decreased successfully"));
    }
}
