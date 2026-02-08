package com.example.day3_java.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequest {
    @NotNull(message = "Yêu cầu id sản phẩm")
    private Long productId;

    @NotNull(message = "Số lượng ko được để trống")
    @Min(value = 1,message = "số lượng phải lớn hơn 0")
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
