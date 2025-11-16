package com.ravemaster.inventory.controller;

import com.ravemaster.inventory.domain.response.DashboardResponse;
import com.ravemaster.inventory.domain.response.Response;
import com.ravemaster.inventory.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
