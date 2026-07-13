package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class SalesSummaryDTO {
    private BigDecimal revenue;
    private Long totalItemsSold;
    private BigDecimal averageOrderValue;
    private BigDecimal totalProfit;
}
