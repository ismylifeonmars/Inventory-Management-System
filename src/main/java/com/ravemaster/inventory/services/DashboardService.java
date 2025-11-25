package com.ravemaster.inventory.services;

import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.response.DashboardResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DashboardService {
    DashboardResponse getDashStatistics();
    List<ProductDto> getLowStockProducts();
    List<ProductDto> getBestSellingProducts();
    List<ProductDto> getWorstSellingProducts();
}
