package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.response.DashboardResponse;
import com.ravemaster.inventory.repository.ProductRepository;
import com.ravemaster.inventory.repository.TransactionRepository;
import com.ravemaster.inventory.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public DashboardResponse getDashStatistics() {

        BigDecimal totalStockValue = productRepository.calculateTotalStockValue();
        ZoneId nairobi = ZoneId.of("Africa/Nairobi");
        LocalDate today = LocalDate.now(nairobi);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        BigDecimal salesPerDay = transactionRepository.calculateTotalSalesPerDay("Sale", start, end);
        BigDecimal cashSalesPerDay = transactionRepository.calculateTotalSalesPerDayPerType("Sale", "Cash", start, end);
        BigDecimal mpesaSalesPerDay = transactionRepository.calculateTotalSalesPerDayPerType("Sale", "MPESA", start, end);
        return DashboardResponse.builder()
                .stockValue(totalStockValue.doubleValue())
                .totalCashSalesPerDay(cashSalesPerDay.doubleValue())
                .totalMpesaSalesPerDay(mpesaSalesPerDay.doubleValue())
                .totalSalesPerDay(salesPerDay.doubleValue())
                .totalPurchases(0.0)
                .totalRevenue(0.0)
                .build();
    }

}
