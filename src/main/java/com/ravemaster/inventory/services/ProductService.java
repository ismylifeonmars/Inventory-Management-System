package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product createProduct(ProductRequest productRequest);
    Product updateProduct(UUID id, ProductRequest productRequest);
    Product getProduct(UUID id);
    void deleteProduct(UUID id);
    Page<ProductDto> listProducts(Pageable pageable);
    Page<ProductDto> findProductByName(Pageable pageable, String name);
    Page<ProductDto> findByCategoryName(Pageable pageable, String categoryName);
}
