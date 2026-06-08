package com.acharya.dikshanta.EcomMed.controller;

import com.acharya.dikshanta.EcomMed.dto.request.OrderFilterRequest;
import com.acharya.dikshanta.EcomMed.dto.request.PageableRequest;
import com.acharya.dikshanta.EcomMed.dto.response.ApiResponse;
import com.acharya.dikshanta.EcomMed.dto.response.OrderResponse;
import com.acharya.dikshanta.EcomMed.dto.response.PagedResponse;
import com.acharya.dikshanta.EcomMed.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder() {
        OrderResponse orderResponse = orderService.placeOrder();
        return ResponseEntity.ok(ApiResponse.success(orderResponse, "Order places successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<PagedResponse<OrderResponse>>> getOrders(
            @ModelAttribute PageableRequest pageableRequest,
            @ModelAttribute OrderFilterRequest request) {
        PagedResponse<OrderResponse> allOrders = orderService.getAllOrders(pageableRequest.toPageable(), request);
        return ResponseEntity.ok(ApiResponse.success(allOrders, "Orders fetched successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrder() {
        List<OrderResponse> orderById = orderService.getOrderById();
        return ResponseEntity.ok(ApiResponse.success(orderById, "Order Fetched successfully"));

    }
}
