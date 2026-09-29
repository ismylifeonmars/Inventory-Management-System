package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.entity.Category;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.request.CategoryRequest;
import com.ravemaster.inventory.domain.request.ProductRequest;
import com.ravemaster.inventory.mapper.CategoryMapper;
import com.ravemaster.inventory.mapper.ProductMapper;
import com.ravemaster.inventory.repository.CategoryRepository;
import com.ravemaster.inventory.repository.ProductRepository;
import com.ravemaster.inventory.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
    public ProductDto createProduct(ProductRequest productRequest) {

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
                .unitPrice(BigDecimal.valueOf(productRequest.getUnitPrice()))
                .build();
        Product savedProduct = productRepository.save(product);

        return mapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public List<ProductDto> uploadProducts(MultipartFile excel) {

        List<ProductRequest> productRequests = new ArrayList<>();
        try {
            InputStream file = excel.getInputStream();
            Workbook workbook = WorkbookFactory.create(file);

            Sheet sheet = workbook.getSheetAt(0);

            sheet.forEach(row -> {

                if (row.getRowNum() == 0) {
                    return;
                }

                Cell nameCell = row.getCell(0);

                if (nameCell == null || nameCell.getCellType() == CellType.BLANK) {
                    return;
                }

                ProductRequest productRequest = ProductRequest.builder()
                        .name(nameCell.getStringCellValue())
                        .stockQuantity((int) row.getCell(1).getNumericCellValue())
                        .description(row.getCell(2).getStringCellValue())
                        .categoryName(row.getCell(3).getStringCellValue())
                        .unitPrice(row.getCell(4).getNumericCellValue())
                        .build();

                productRequests.add(productRequest);
            });

        } catch (IOException e){
            throw new IllegalStateException(e.getMessage());
        }

        List<Product> products = new ArrayList<>();

        for (ProductRequest request: productRequests){
            Optional<Category> categoryByName = categoryRepository.findCategoryByName(request.getCategoryName());

            Product product = Product.builder().build();
            if(categoryByName.isPresent()){
                product.setCategory(categoryByName.get());
            } else {
                CategoryRequest categoryRequest = CategoryRequest
                        .builder()
                        .name(request.getCategoryName())
                        .build();
                Category entity = categoryMapper.toEntity(categoryRequest);
                Category saved = categoryRepository.save(entity);
                product.setCategory(saved);
            }

            Optional<Product> productByName = productRepository.findProductByName(request.getName());

            if (productByName.isEmpty()) {
                product.setName(request.getName());
                product.setDescription(request.getDescription());
                product.setStockQuantity(request.getStockQuantity());
                product.setUnitPrice(BigDecimal.valueOf(request.getUnitPrice()));
                products.add(product);
            }
        }

        List<Product> savedProducts = productRepository.saveAll(products);

        return savedProducts.stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional
    public ProductDto updateProduct(UUID id, ProductRequest productRequest) {
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
        product.setUnitPrice(BigDecimal.valueOf(productRequest.getUnitPrice()));

        Product updatedProduct = productRepository.save(product);
        return mapper.toDto(updatedProduct);
    }

    @Override
    public ProductDto getProduct(UUID id) {

        Product retreivedProduct = productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Could not find product with id: " + id
                )
        );
        CategoryDto categoryDto = categoryMapper.toDto(retreivedProduct.getCategory());
        ProductDto dto = mapper.toDto(retreivedProduct);
        dto.setCategoryName(categoryDto.getName());
        return dto;
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductDto> listProducts(Pageable pageable) {
        Page<UUID> allProductIds = productRepository.findAllProductIds(pageable);
        return new PageImpl<>(getList(allProductIds, pageable),pageable, allProductIds.getTotalElements());
    }

    @Override
    public Page<ProductDto> findProductByName(Pageable pageable, String name) {
        Page<UUID> allProductIds = productRepository.findAllProductIdsByName(name,pageable);
        return new PageImpl<>(getList(allProductIds, pageable),pageable, allProductIds.getTotalElements());
    }

    @Override
    public Page<ProductDto> findByCategoryName(Pageable pageable, String categoryName) {
        Page<UUID> allProductIds = productRepository.findAllProductIdsByCategory(categoryName,pageable);
        return new PageImpl<>(getList(allProductIds, pageable),pageable, allProductIds.getTotalElements());
    }

    private List<ProductDto> getList(Page<UUID> idList, Pageable pageable){
        List<Product> byIds = productRepository.findByIds(idList.getContent(), pageable.getSort());

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
        return productDtos;
    }
}
