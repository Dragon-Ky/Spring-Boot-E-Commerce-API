package com.example.day3_java.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    // N item thuộc 1 order

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    // N item thuộc 1 product

    @Column(nullable = false)
    private Integer quantity;
    // số lượng mua

    @Column(nullable = false)
    private Double unitPrice;
    // giá tại thời điểm mua (không lấy trực tiếp từ product khi xem lại)
}
