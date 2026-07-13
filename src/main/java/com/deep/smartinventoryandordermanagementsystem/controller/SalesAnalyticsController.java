package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.dashboard.MonthlyRevenueDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.dashboard.SalesSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.service.DashboardService;
import com.deep.smartinventoryandordermanagementsystem.service.SalesAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/analytics/sales")
@PreAuthorize("hasRole('ADMIN')")
public class SalesAnalyticsController {
    private final SalesAnalyticsService salesAnalyticsService;
    private final DashboardService dashboardService;

    public SalesAnalyticsController(
            SalesAnalyticsService salesAnalyticsService, DashboardService dashboardService) {

        this.salesAnalyticsService = salesAnalyticsService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<SalesSummaryDTO> getSummary() {
        return ResponseEntity.ok(
                salesAnalyticsService.getSalesSummary()
        );
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueDTO>>
    getMonthlyRevenue() {

        return ResponseEntity.ok(
                dashboardService.getMonthlyRevenue()
        );
    }
}
