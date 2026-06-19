package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardSummaryDTO {
    private long totalProducts;
    private long totalStock;
    private long lowStockProducts;
    private long outOfStockProducts;
    private BigDecimal inventoryCostValue;
    private BigDecimal inventorySellingValue;
    private BigDecimal potentialProfit;


    private long totalOrders;
    private long pendingOrders;
    private long confirmedOrders;
    private long shippedOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private List<RecentOrderDTO> recentOrders;

    private BigDecimal revenue;
    private List<MonthlyRevenueDTO> monthlyRevenue;
    private List<TopSellingProductDTO> topSellingProducts;







}
