package com.example.day3_java.specification;

import com.example.day3_java.entity.Product;
import org.springframework.data.jpa.domain.Specification;

// này tự lấy data
public class ProductSpecification {

    //LOWER(name) LIKE %keyword%
    public static Specification<Product> nameContains(String keyword){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%"
                );

    }
    //price >= minPrice
    public static Specification<Product> priceGte(Double minPrice){
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThanOrEqualTo(root.get("price"),minPrice);

    }
    //price <= maxPrice
    public static Specification<Product> priceLte(Double maxPrice){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"),maxPrice);
    }
}
