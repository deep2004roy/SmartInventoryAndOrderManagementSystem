package com.deep.smartinventoryandordermanagementsystem.dto.analytics;

import lombok.Data;

@Data
public class CustomerAnalyticsSummaryDTO {
    private long totalCustomers;

    private long customersWithOrders;

    private long repeatCustomers;
}
