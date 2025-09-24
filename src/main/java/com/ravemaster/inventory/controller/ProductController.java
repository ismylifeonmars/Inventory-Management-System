package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.dto.TransactionLineDto;
import com.ravemaster.inventory.domain.entity.Category;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.request.ProductRequest;
import com.ravemaster.inventory.domain.response.PaginationResponse;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.mapper.CategoryMapper;
import com.ravemaster.inventory.mapper.ProductMapper;
import com.ravemaster.inventory.mapper.TransactionLineMapper;
import com.ravemaster.inventory.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper mapper;
    private final CategoryMapper categoryMapper;
    private final TransactionLineMapper lineMapper;

    @PostMapping
    public ResponseEntity<Response> createProduct(
            @Valid @RequestBody ProductRequest productRequest
            ){
        Product product = productService.createProduct(productRequest);
        ProductDto dto = mapper.toDto(product);
        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Success")
                .product(dto)
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Response> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest productRequest
    ){
        Product product = productService.updateProduct(id,productRequest);
        ProductDto dto = mapper.toDto(product);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .product(dto)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Response> getProduct(
            @PathVariable UUID id
    ){
        Product product = productService.getProduct(id);
        CategoryDto categoryDto = categoryMapper.toDto(product.getCategory());
        ProductDto dto = mapper.toDto(product);
        dto.setCategoryName(categoryDto.getName());
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .product(dto)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Response> deleteProduct(
            @PathVariable UUID id
    ){
        productService.deleteProduct(id);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getAllProducts(Pageable pageable){
        Page<ProductDto> productList = productService.listProducts(pageable);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(productList.getTotalElements())
                .totalPages(productList.getTotalPages())
                .currentPage(productList.getNumber())
                .pageSize(productList.getSize())
                .build();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .paginationResponse(paginationResponse)
                .products(productList.getContent())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
