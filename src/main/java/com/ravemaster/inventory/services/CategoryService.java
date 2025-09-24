package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.request.CategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto save(CategoryRequest categoryRequest);
    List<CategoryDto> listAll();
    CategoryDto getCategory(UUID id);
    CategoryDto updateCategory(UUID id, CategoryRequest categoryRequest);
    void deleteCategory(UUID id);

}
