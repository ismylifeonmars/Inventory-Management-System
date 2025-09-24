package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.entity.Category;
import com.ravemaster.inventory.domain.request.CategoryRequest;
import com.ravemaster.inventory.mapper.CategoryMapper;
import com.ravemaster.inventory.repository.CategoryRepository;
import com.ravemaster.inventory.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto save(CategoryRequest categoryRequest) {
        Category entity = mapper.toEntity(categoryRequest);
        Category saved = categoryRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public List<CategoryDto> listAll() {
        List<Category> categories = categoryRepository.findAllWithProductCount();
        return categories.stream().map(mapper::toDto).toList();
    }

    @Override
    public CategoryDto getCategory(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Category does not exist with id: " + id
                )
        );
        return mapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Category does not exist with id: " + id
                )
        );
        existingCategory.setName(categoryRequest.getName());
        Category saved = categoryRepository.save(existingCategory);
        return mapper.toDto(saved);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()){
            if (!category.get().getProducts().isEmpty()){
                throw new IllegalStateException("This category has products listed under it.");
            }
            categoryRepository.deleteById(id);
        }
    }
}
