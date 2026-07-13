package com.deep.smartinventoryandordermanagementsystem.dto.analytics;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitSummaryDTO {
    private BigDecimal revenue;

    private BigDecimal profit;

    private BigDecimal margin;
}
