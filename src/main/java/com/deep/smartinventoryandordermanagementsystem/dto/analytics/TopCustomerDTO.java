package com.deep.smartinventoryandordermanagementsystem.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopCustomerDTO {
    private String customerName;

    private Long totalOrders;

    private BigDecimal totalSpent;
}
