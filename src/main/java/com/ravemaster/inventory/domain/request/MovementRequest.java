package com.ravemaster.inventory.domain.request;

import com.ravemaster.inventory.domain.enums.MovementType;
import com.ravemaster.inventory.domain.enums.ReferenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementRequest {
    private MovementType movementType;
    private ReferenceType referenceType;
    private UUID referenceId;
    private String performedBy;
    private String reason;
    private List<MovementLineRequest> movementLines;
}
