package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardSummaryDTO {
    private InventorySummaryDTO inventorySummary;
    private SalesSummaryDTO salesSummary;
    private OrderSummaryStatsDTO orderSummary;
    private List<MonthlyRevenueDTO> monthlyRevenue;
    private List<TopSellingProductDTO> topSellingProducts;
    private List<RecentOrderDTO> recentOrders;
}
