package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import lombok.Data;

@Data
public class OrderSummaryStatsDTO {
    private long totalOrders;
    private long pendingOrders;
    private long confirmedOrders;
    private long shippedOrders;
    private long deliveredOrders;
    private long cancelledOrders;
}
