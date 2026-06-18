package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.DashboardSummary;
import com.deep.smartinventoryandordermanagementsystem.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;

public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

//    @GetMapping("/dashboard/summary")
//    public DashboardSummary getSummary(){
//       return dashboardService.getSummary();
//    }

}
