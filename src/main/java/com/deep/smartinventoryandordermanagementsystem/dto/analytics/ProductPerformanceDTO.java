package com.deep.smartinventoryandordermanagementsystem.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPerformanceDTO {
    private String productName;
    private Long unitsSold;
}
