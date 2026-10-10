package com.ravemaster.inventory.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovementLineDto {
    private UUID id;
    private Integer quantity;
    private Integer previousQuantity;
    private Integer resultingQuantity;
    private UUID productId;
    private UUID movementId;
}
