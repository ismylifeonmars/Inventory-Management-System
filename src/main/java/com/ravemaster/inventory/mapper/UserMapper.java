package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.UserDto;
import com.ravemaster.inventory.domain.entity.Transaction;
import com.ravemaster.inventory.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(source = "transactions", target = "transactionsCount", qualifiedByName = "getTransactionsCount")
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    @Named("getTransactionsCount")
    default long getTransactionCount(List<Transaction> transactions){
        if (transactions.isEmpty()){
            return 0;
        }
        return transactions.size();
    }
}
