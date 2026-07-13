package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class InventorySummaryDTO {
    private long totalProducts;
    private long totalStock;
    private long lowStockProducts;
    private long outOfStockProducts;
    private BigDecimal inventoryCostValue;
    private BigDecimal inventorySellingValue;
    private BigDecimal potentialProfit;
}
