package com.deep.smartinventoryandordermanagementsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class DashboardSummary {
    private int totalProducts;
    private int totalStock;
    private int totalOrders;
    private double revenue;
    private List<LowStockProductDto> lowStockProducts;
    private List<RecentOrderDto> recentOrders;
    private long pendingOrders;
    private long confirmedOrders;
    private long shippedOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private List<MonthlyRevenueDto> monthlyRevenue;
    private List<TopSellingProductDto> topSellingProducts;
}
