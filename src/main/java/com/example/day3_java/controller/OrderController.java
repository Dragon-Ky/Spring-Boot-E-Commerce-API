package com.example.day3_java.controller;

import com.example.day3_java.dto.ApiResponse;
import com.example.day3_java.dto.request.OrderCreateRequest;
import com.example.day3_java.dto.response.OrderResponse;
import com.example.day3_java.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.oke(orderService.create(request));
    }
    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getById(@PathVariable Long id){
        return ApiResponse.oke(orderService.getById(id));
    }
    @PutMapping("/{id}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable Long id){
        return ApiResponse.oke(orderService.cancel(id));
    }

    @GetMapping("/my")
    public ApiResponse<List<OrderResponse>>myOrders(){
        return ApiResponse.oke(orderService.myOrders());
    }
}

