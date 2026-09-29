package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto createProduct(ProductRequest productRequest);
    List<ProductDto> uploadProducts(MultipartFile excel);
    ProductDto updateProduct(UUID id, ProductRequest productRequest);
    ProductDto getProduct(UUID id);
    void deleteProduct(UUID id);
    Page<ProductDto> listProducts(Pageable pageable);
    Page<ProductDto> findProductByName(Pageable pageable, String name);
    Page<ProductDto> findByCategoryName(Pageable pageable, String categoryName);
}
