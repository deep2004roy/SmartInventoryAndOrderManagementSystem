package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyRevenueDTO {
    private String month;
    private BigDecimal revenue;
}
