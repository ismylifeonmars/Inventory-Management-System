package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.TransactionLineDto;
import com.ravemaster.inventory.domain.entity.TransactionLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionLineMapper {
    TransactionLine toEntity(TransactionLineDto transactionLineDto);

    @Mapping(source = "product.id", target = "productId", defaultValue = "")
    @Mapping(source = "transaction.id", target = "transactionId", defaultValue = "")
    TransactionLineDto toDto(TransactionLine transactionLine);
}
