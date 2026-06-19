package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSellingProductDTO {
    private String productName;
    private Long totalSold;
}
