package com.ravemaster.inventory.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class TransactionDtoSecond {

    private UUID id;
    private String transactionType;
    private String saleType;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private List<TransactionLineDto> transactionLines;
    private String name;
}
