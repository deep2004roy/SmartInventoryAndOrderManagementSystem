package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.ProductPerformanceDTO;
import com.deep.smartinventoryandordermanagementsystem.service.ProductAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class ProductAnalyticsController {
    private final ProductAnalyticsService service;

    public ProductAnalyticsController(
            ProductAnalyticsService service) {

        this.service = service;
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<ProductPerformanceDTO>>
    getTopSelling() {

        return ResponseEntity.ok(
                service.getTopSellingProducts()
        );
    }

    @GetMapping("/least-selling")
    public ResponseEntity<List<ProductPerformanceDTO>>
    getLeastSelling() {

        return ResponseEntity.ok(
                service.getLeastSellingProducts()
        );
    }
}
