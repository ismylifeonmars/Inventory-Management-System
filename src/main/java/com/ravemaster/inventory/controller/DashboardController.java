package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.dto.ProductDto;
import com.ravemaster.inventory.domain.response.DashboardResponse;
import com.ravemaster.inventory.domain.response.PaginationResponse;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/dashboard/")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getDashboardStatistics(){
        DashboardResponse dashStatistics = dashboardService.getDashStatistics();
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .dashboardResponse(dashStatistics)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<Response> getLowStockProducts(Pageable pageable, @RequestParam("min") Integer min, @RequestParam("max") Integer max){
        Page<ProductDto> productByName = dashboardService.getLowStockProducts(pageable, min, max);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(productByName.getTotalElements())
                .totalPages(productByName.getTotalPages())
                .currentPage(productByName.getNumber())
                .pageSize(productByName.getSize())
                .build();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .paginationResponse(paginationResponse)
                .products(productByName.getContent())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/best-selling")
    public ResponseEntity<Response> getBestSellingProducts(Pageable pageable){
        Page<ProductDto> productByName = dashboardService.getBestSellingProducts(pageable);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(productByName.getTotalElements())
                .totalPages(productByName.getTotalPages())
                .currentPage(productByName.getNumber())
                .pageSize(productByName.getSize())
                .build();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .paginationResponse(paginationResponse)
                .products(productByName.getContent())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/worst-selling")
    public ResponseEntity<Response> getWorstSellingProducts(Pageable pageable){
        Page<ProductDto> productByName = dashboardService.getWorstSellingProducts(pageable);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalElements(productByName.getTotalElements())
                .totalPages(productByName.getTotalPages())
                .currentPage(productByName.getNumber())
                .pageSize(productByName.getSize())
                .build();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .paginationResponse(paginationResponse)
                .products(productByName.getContent())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
