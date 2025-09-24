package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.entity.Category;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.request.ProductRequest;
import com.ravemaster.inventory.mapper.CategoryMapper;
import com.ravemaster.inventory.mapper.ProductMapper;
import com.ravemaster.inventory.repository.CategoryRepository;
import com.ravemaster.inventory.repository.ProductRepository;
import com.ravemaster.inventory.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Product createProduct(ProductRequest productRequest) {

        Category categoryByName = categoryRepository.findCategoryByName(productRequest.getCategoryName()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find Category with name: "+productRequest.getCategoryName()
                )
        );
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .stockQuantity(productRequest.getStockQuantity())
                .category(categoryByName)
                .unitPrice(productRequest.getUnitPrice())
                .build();

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find product with id: " + id
                )
        );

        Category categoryByName = categoryRepository.findCategoryByName(productRequest.getCategoryName()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find Category with name: "+productRequest.getCategoryName()
                )
        );

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(categoryByName);
        product.setUnitPrice(productRequest.getUnitPrice());
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Could not find product with id: "+id
                )
        );
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductDto> listProducts(Pageable pageable) {
        Page<Long> allProductIds = productRepository.findAllProductIds(pageable);
        List<Product> byIds = productRepository.findByIds(allProductIds.getContent());

        List<Category> categoryList = new ArrayList<>();
        for (Product product: byIds){
            categoryList.add(product.getCategory());
        }
        List<CategoryDto> categoryDtos = categoryList.stream().map(categoryMapper::toDto).toList();
        List<ProductDto> productDtos = byIds.stream().map(mapper::toDto).toList();

        IntStream.range(0, productDtos.size())
                .forEach( i -> {
                    CategoryDto categoryDto = categoryDtos.get(i % categoryDtos.size());
                    productDtos.get(i).setCategoryName(categoryDto.getName());
                });

        return new PageImpl<>(productDtos,pageable, allProductIds.getTotalElements());
    }
}
