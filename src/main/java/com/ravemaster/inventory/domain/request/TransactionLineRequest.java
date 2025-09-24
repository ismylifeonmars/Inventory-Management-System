package com.ravemaster.inventory.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLineRequest {

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Unit price cannot be null")
    private Double unitPrice;

    @NotNull(message = "Product id cannot be null")
    private UUID productId;

}
