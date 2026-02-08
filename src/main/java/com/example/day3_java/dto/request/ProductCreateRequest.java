package com.example.day3_java.dto.request;

import jakarta.validation.constraints.*;

public class ProductCreateRequest {
    @NotBlank(message = "Ten ko de trong")
    private String name;

    @NotNull(message = "Yeu cau dien so luong")
    @Min(value = 0 ,message = "so luong ko duoc am")
    private Integer stock;

    @NotNull(message = "Yeu cau dien gia tien")
    @DecimalMin(value = "0.01",message = "gia phai lon hon 0")
    private Double price;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
