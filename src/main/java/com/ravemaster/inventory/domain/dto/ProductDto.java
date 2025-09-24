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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    private UUID id;
    private String name;
    private Integer stockQuantity;
    private String description;
    private Double unitPrice;
    private LocalDateTime createdAt;
    private String categoryName;
    private int transactionLinesCount;

}
