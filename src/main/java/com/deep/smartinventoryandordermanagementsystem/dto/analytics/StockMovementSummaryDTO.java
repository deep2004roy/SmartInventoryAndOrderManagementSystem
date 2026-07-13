package com.deep.smartinventoryandordermanagementsystem.dto.analytics;

import lombok.Data;

@Data
public class StockMovementSummaryDTO {
    private Long purchases;

    private Long sales;

    private Long returns;

    private Long adjustments;
}
