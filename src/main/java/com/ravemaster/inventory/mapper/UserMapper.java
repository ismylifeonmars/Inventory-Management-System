package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.UserDto;
import com.ravemaster.inventory.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
