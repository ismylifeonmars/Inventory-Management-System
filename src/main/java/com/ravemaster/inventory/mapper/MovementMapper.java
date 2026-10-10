package com.ravemaster.inventory.mapper;

import com.ravemaster.inventory.domain.dto.MovementDto;
import com.ravemaster.inventory.domain.dto.MovementDtoSecond;
import com.ravemaster.inventory.domain.entity.Movement;
import com.ravemaster.inventory.domain.entity.MovementLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MovementMapper {

    @Mapping(source = "performedBy.email", target = "performedBy")
    @Mapping(source = "movementLines", target = "movementLinesCount", qualifiedByName = "calculateMovementLines")
    MovementDto toDto(Movement movement);

    @Mapping(source = "performedBy.email", target = "performedBy")
    MovementDtoSecond toDtoSecond(Movement movement);

    @Named("calculateMovementLines")
    default long calculateMovementLines(List<MovementLine> lines){
        if (lines == null){
            return  0;
        }
        return lines.size();
    }

}
