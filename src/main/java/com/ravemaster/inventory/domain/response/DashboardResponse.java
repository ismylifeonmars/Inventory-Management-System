package com.ravemaster.inventory.domain.response;

import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private double stockValue;
    private double totalSalesPerDay;
    private double totalCashSalesPerDay;
    private double totalMpesaSalesPerDay;
}
