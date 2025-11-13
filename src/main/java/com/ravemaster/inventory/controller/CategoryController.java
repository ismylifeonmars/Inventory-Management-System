package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.entity.Category;
import com.ravemaster.inventory.domain.request.CategoryRequest;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.mapper.CategoryMapper;
import com.ravemaster.inventory.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
            ){
        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Success")
                .category(categoryService.save(categoryRequest))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getCategory(
            @PathVariable UUID id
            ){
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .category(categoryService.getCategory(id))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest categoryRequest
    ){
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .category(categoryService.updateCategory(id,categoryRequest))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteCategory(
            @PathVariable UUID id
    ){
        categoryService.deleteCategory(id);
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllCategories(){
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .categories(categoryService.listAll())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
