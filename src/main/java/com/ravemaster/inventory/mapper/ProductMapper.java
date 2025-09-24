package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.entity.TransactionLine;
import com.ravemaster.inventory.domain.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    @Mapping(source = "category.name", target = "categoryName", defaultValue = "")
    @Mapping(source = "transactionLines", target = "transactionLinesCount", qualifiedByName = "calculateTransactionLinesCount")
    ProductDto toDto(Product product);

    Product toEntity(ProductRequest productRequest);

    @Named("calculateTransactionLinesCount")
    default long calculateTransactionLinesCount(List<TransactionLine> transactionLines){
        if (transactionLines == null){
            return 0;
        }
        return transactionLines.size();
    }
}
