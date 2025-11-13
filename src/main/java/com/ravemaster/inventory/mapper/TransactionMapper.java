package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.TransactionDto;
import com.ravemaster.inventory.domain.dto.TransactionDtoSecond;
import com.ravemaster.inventory.domain.entity.Transaction;
import com.ravemaster.inventory.domain.entity.TransactionLine;
import com.ravemaster.inventory.domain.request.TransactionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {
    Transaction toEntity(TransactionRequest transactionRequest);

    @Mapping(source = "transactionLines", target = "transactionLinesCount", qualifiedByName = "calculateTransactionLines")
    @Mapping(source = "user.name", target = "name", defaultValue = "")
    TransactionDto toDto(Transaction transaction);

    @Mapping(source = "user.name", target = "name", defaultValue = "")
    TransactionDtoSecond toDtoSecond(Transaction transaction);

    @Named("calculateTransactionLines")
    default long calculateTransactionLines(List<TransactionLine> transactionLines){
        if (transactionLines == null){
            return 0;
        }
        return transactionLines.size();
    }
}
