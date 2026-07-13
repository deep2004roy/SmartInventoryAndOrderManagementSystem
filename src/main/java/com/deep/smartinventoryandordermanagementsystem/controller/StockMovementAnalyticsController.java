package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.StockMovementSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.model.StockMovement;
import com.deep.smartinventoryandordermanagementsystem.service.StockMovementAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/analytics/stock-movements")
@PreAuthorize("hasRole('ADMIN')")
public class StockMovementAnalyticsController {
    private final StockMovementAnalyticsService service;

    public StockMovementAnalyticsController(
            StockMovementAnalyticsService service) {

        this.service = service;
    }

    @GetMapping("/summary")
    public ResponseEntity<StockMovementSummaryDTO>
    getSummary() {

        return ResponseEntity.ok(
                service.getSummary()
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovement>>
    getProductHistory(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                service.getProductHistory(
                        productId
                )
        );
    }
}
