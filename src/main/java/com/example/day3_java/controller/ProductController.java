package com.example.day3_java.controller;


import com.example.day3_java.dto.ApiResponse;
import com.example.day3_java.dto.request.ProductCreateRequest;
import com.example.day3_java.dto.response.ProductResponse;
import com.example.day3_java.entity.Product;
import com.example.day3_java.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){this.productService = productService;}

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request){
        return ApiResponse.oke(productService.create(request));
    }
    @GetMapping("api/products/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable Long id) {
        return ApiResponse.oke(productService.getById(id));
    }

    // GET /api/products?page=0&size=5&sort=price,desc
    @GetMapping
    public ApiResponse<Page<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        return ApiResponse.oke(productService.getAll(page, size, sort));
    }
    // GET /api/products/filter?keyword=lap&minPrice=100&maxPrice=2000&page=0&size=5
    @GetMapping("/filter")
    public ApiResponse<Page<ProductResponse>> filter(
            @RequestParam(required = false)String keyword,
            @RequestParam(required = false)Double minPrice,
            @RequestParam(required = false)Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        return ApiResponse.oke(productService.filter(keyword, minPrice, maxPrice, page, size));
    }
}
