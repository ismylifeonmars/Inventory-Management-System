package com.ravemaster.inventory.services.impl;

import com.ravemaster.inventory.domain.dto.CategoryDto;
import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.entity.Category;
import com.ravemaster.inventory.domain.entity.Product;
import com.ravemaster.inventory.domain.response.DashboardResponse;
import com.ravemaster.inventory.mapper.CategoryMapper;
import com.ravemaster.inventory.mapper.ProductMapper;
import com.ravemaster.inventory.repository.ProductRepository;
import com.ravemaster.inventory.repository.TransactionRepository;
import com.ravemaster.inventory.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final ProductMapper mapper;
    private final CategoryMapper categoryMapper;

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
                .build();
    }

    @Override
    public Page<ProductDto> getLowStockProducts(Pageable pageable, Integer min, Integer max) {
        Page<UUID> lowStockProductsIds = productRepository.findLowStockProductsIds(min, max, pageable);
        return new PageImpl<>(getList(lowStockProductsIds,pageable),pageable, lowStockProductsIds.getTotalElements());
    }

    @Override
    public Page<ProductDto> getBestSellingProducts(Pageable pageable) {
        Page<UUID> bestSellingProductIds = productRepository.findBestSellingProductIds(pageable);
        return new PageImpl<>(getList(bestSellingProductIds,pageable),pageable,bestSellingProductIds.getTotalElements());
    }

    @Override
    public Page<ProductDto> getWorstSellingProducts(Pageable pageable) {
        Page<UUID> worstSellingProductIds = productRepository.findWorstSellingProductIds(pageable);
        return new PageImpl<>(getList(worstSellingProductIds,pageable),pageable,worstSellingProductIds.getTotalElements());
    }

    private List<ProductDto> getList(Page<UUID> idList, Pageable pageable){
        List<Product> byIds = productRepository.findByIdsSecond(idList.getContent());

        List<Category> categoryList = new ArrayList<>();
        for (Product product: byIds){
            categoryList.add(product.getCategory());
        }

        List<CategoryDto> categoryDtos = categoryList.stream().map(categoryMapper::toDto).toList();
        List<ProductDto> productDtos = byIds.stream().map(mapper::toDto).toList();

        IntStream.range(0, productDtos.size())
                .forEach( i -> {
                    CategoryDto categoryDto = categoryDtos.get(i % categoryDtos.size());
                    productDtos.get(i).setCategoryName(categoryDto.getName());
                });
        return productDtos;
    }

}
