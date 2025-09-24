package com.ravemaster.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionLineDto {
    private UUID id;
    private Integer quantity;
    private Double unitPrice;
    private Double lineTotal;
    private UUID productId;
    private UUID transactionId;
}
