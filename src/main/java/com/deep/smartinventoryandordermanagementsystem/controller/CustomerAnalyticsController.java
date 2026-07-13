package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.CustomerAnalyticsSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.analytics.TopCustomerDTO;
import com.deep.smartinventoryandordermanagementsystem.service.CustomerAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/analytics/customers")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerAnalyticsController {
    private final CustomerAnalyticsService service;

    public CustomerAnalyticsController(
            CustomerAnalyticsService service) {

        this.service = service;
    }

    @GetMapping("/summary")
    public ResponseEntity<CustomerAnalyticsSummaryDTO>
    getSummary() {

        return ResponseEntity.ok(
                service.getSummary()
        );
    }

    @GetMapping("/top")
    public ResponseEntity<List<TopCustomerDTO>>
    getTopCustomers() {

        return ResponseEntity.ok(
                service.getTopCustomers()
        );
    }
}
