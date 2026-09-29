package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.request.ProductRequest;
import com.ravemaster.inventory.domain.response.PaginationResponse;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.mapper.CategoryMapper;
import com.ravemaster.inventory.mapper.ProductMapper;
import com.ravemaster.inventory.mapper.TransactionLineMapper;
import com.ravemaster.inventory.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper mapper;
    private final CategoryMapper categoryMapper;
    private final TransactionLineMapper lineMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> createProduct(
            @Valid @RequestBody ProductRequest productRequest
            ){
        ProductDto dto = productService.createProduct(productRequest);
        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Success")
                .product(dto)
                .build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Upload excel file with products")
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> uploadProducts(
            @Parameter(description = "The excel file to upload")
            @RequestParam("file")MultipartFile excel
            ){
        List<ProductDto> productDtos = productService.uploadProducts(excel);

        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Products uploaded successfully")
                .products(productDtos)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest productRequest
    ){
        ProductDto dto = productService.updateProduct(id,productRequest);
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
        ProductDto dto = productService.getProduct(id);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .product(dto)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/name")
    public ResponseEntity<Response> getAllProductsByName(Pageable pageable, @RequestParam("name") String name){
        Page<ProductDto> productByName = productService.findProductByName(pageable, name);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(productByName.getTotalElements())
                .totalPages(productByName.getTotalPages())
                .currentPage(productByName.getNumber())
                .pageSize(productByName.getSize())
                .build();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .paginationResponse(paginationResponse)
                .products(productByName.getContent())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/category-name")
    public ResponseEntity<Response> getProductsByCategoryName(Pageable pageable, @RequestParam("categoryName") String categoryName){
        Page<ProductDto> productByCategoryName = productService.findByCategoryName(pageable,categoryName);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(productByCategoryName.getTotalElements())
                .totalPages(productByCategoryName.getTotalPages())
                .currentPage(productByCategoryName.getNumber())
                .pageSize(productByCategoryName.getSize())
                .build();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .paginationResponse(paginationResponse)
                .products(productByCategoryName.getContent())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
