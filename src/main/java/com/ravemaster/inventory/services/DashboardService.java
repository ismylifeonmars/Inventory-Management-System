package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.response.DashboardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DashboardService {
    DashboardResponse getDashStatistics();
    Page<ProductDto> getLowStockProducts(Pageable pageable, Integer min, Integer max);
    Page<ProductDto> getBestSellingProducts(Pageable pageable);
    Page<ProductDto> getWorstSellingProducts(Pageable pageable);
}
