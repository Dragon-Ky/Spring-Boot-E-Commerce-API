package com.example.day3_java.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    // thời điểm tạo đơn

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    // 1 order có nhiều item
    // cascade ALL: lưu order sẽ lưu luôn items
    // orphanRemoval: xóa item khỏi list thì DB cũng xóa
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;
    // EnumType.STRING: lưu "NEW", "PAID"... (dễ đọc, an toàn hơn ORDINAL)
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
}
