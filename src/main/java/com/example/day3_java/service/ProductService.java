package com.example.day3_java.service;

import com.example.day3_java.dto.request.ProductCreateRequest;
import com.example.day3_java.dto.response.ProductResponse;
import com.example.day3_java.entity.Product;
import com.example.day3_java.exception.ResourceNotFoundException;
import com.example.day3_java.repository.ProductRepository;
import com.example.day3_java.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.domain.Specification;


import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){this.productRepository = productRepository;}
    //Mapping
    public ProductResponse toResponse(Product product){
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setStock(product.getStock());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        return response;
    }
    public ProductResponse create(ProductCreateRequest request){
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }
    public ProductResponse getById(Long id){
        Product p = productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("ko tim thay id : "+id));
        return toResponse(p);
    }

    public Page<ProductResponse> getAll(int page,int size,String sort){
        // sort format: "price,desc" hoặc "id,asc"
        String[] sortArr = sort.split(",");
        String sortField = sortArr[0];// ví dụ "price"
        String sortDir = (sortArr.length > 1)? sortArr[1]:"asc"; // mặc định asc

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,sortField));

        return productRepository.findAll(pageable).map(this::toResponse);
    }
    //FILTER keyword + minPrice + maxPrice + pagination
    public Page<ProductResponse> filter(String keyword,Double minPrice,Double maxPrice,int page,int size){
        // spec ban đầu = null (không filter gì)
        //Specification<Product> spec = Specification.where(null);
        Specification<Product> spec = (root, query, cb) -> cb.conjunction();
        if (keyword != null && !keyword.isBlank()){
            spec = spec.and(ProductSpecification.nameContains(keyword));
        }
        if (minPrice !=null){
            spec = spec.and(ProductSpecification.priceGte(minPrice));
        }
        if (maxPrice != null){
            spec=spec.and(ProductSpecification.priceLte(maxPrice));
        }
        Pageable pageable = PageRequest.of(page,size);
        return productRepository.findAll(spec,pageable).map(this::toResponse);
    }
}
