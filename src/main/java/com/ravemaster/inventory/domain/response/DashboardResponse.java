package com.ravemaster.inventory.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private double stockValue;
    private double totalSalesPerDay;
    private double totalCashSalesPerDay;
    private double totalMpesaSalesPerDay;
    private double totalPurchases;
    private double totalRevenue;
}
