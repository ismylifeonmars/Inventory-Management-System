package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.MovementLineDto;
import com.ravemaster.inventory.domain.entity.MovementLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MovementLineMapper {

    @Mapping(source = "product.id", target = "productId", defaultValue = "")
    @Mapping(source = "movement.id", target = "movementId", defaultValue = "")
    MovementLineDto toDto(MovementLine movementLine);
}
