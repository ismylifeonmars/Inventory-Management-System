package com.ravemaster.inventory.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TransactionRequest {

    @NotBlank(message = "Transaction type must be included")
    private String transactionType;

    @NotBlank(message = "Sale type must be included")
    private String saleType;

    @NotNull(message = "User id must be provided")
    private String name;

    @NotNull(message = "Transaction line cannot be null")
    private List<TransactionLineRequest> transactionLineRequests;
}
