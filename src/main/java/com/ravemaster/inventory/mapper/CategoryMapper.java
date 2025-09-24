package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.entity.Category;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "products", target = "productSize", qualifiedByName = "calculateProductsSize")
    CategoryDto toDto(Category category);
    Category toEntity(CategoryRequest categoryRequest);

    @Named("calculateProductsSize")
    default long calculateProductsSize(List<Product> products){
        if (products == null){
            return 0;
        }
        return products.size();
    }
}
