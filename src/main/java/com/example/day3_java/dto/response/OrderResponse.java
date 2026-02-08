package com.example.day3_java.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> item;
    private Double total;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getItem() {
        return item;
    }

    public void setItem(List<OrderItemResponse> item) {
        this.item = item;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
