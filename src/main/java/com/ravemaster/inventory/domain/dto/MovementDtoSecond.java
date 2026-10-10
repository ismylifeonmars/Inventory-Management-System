package com.ravemaster.inventory.domain.dto;

import com.ravemaster.inventory.domain.enums.MovementType;
import com.ravemaster.inventory.domain.enums.ReferenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementDtoSecond {
    private UUID id;
    private MovementType movementType;
    private ReferenceType referenceType;
    private UUID referenceId;
    private String reason;
    private LocalDateTime createdAt;
    private String performedBy;
    private List<MovementLineDto> movementLines;
}
