package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.ProfitSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.service.ProfitAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/analytics/profit")
@PreAuthorize("hasRole('ADMIN')")
public class ProfitAnalyticsController {
    private final ProfitAnalyticsService service;

    public ProfitAnalyticsController(
            ProfitAnalyticsService service) {

        this.service = service;
    }

    @GetMapping("/summary")
    public ResponseEntity<ProfitSummaryDTO>
    getProfitSummary() {

        return ResponseEntity.ok(
                service.getProfitSummary()
        );
    }
}
