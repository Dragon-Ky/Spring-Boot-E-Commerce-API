package com.example.day3_java.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderCreateRequest {
    @NotEmpty(message = "sản phẩm ko được trống")
    private List<@Valid OrderItemRequest> items;
    // @Valid để validate từng item bên trong list


    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
