package com.example.day3_java.repository;

import com.example.day3_java.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.items i
        LEFT JOIN FETCH i.product
        WHERE o.id = :id
""")
    Optional<Order> findByIdWithItems(@Param("id") Long id);
    // Fetch join: lấy luôn items và product, tránh LazyInitializationException
    @Query("""
        SELECT DISTINCT o FROM Order o
        LEFT JOIN FETCH o.items i
        LEFT JOIN FETCH i.product
        JOIN o.user u
        WHERE u.username = :username
        ORDER BY o.id DESC
""")
    List<Order> findAllByUsernameWithItems(@Param("username")String username);

}
