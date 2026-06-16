package com.deep.smartinventoryandordermanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSellingProductDto {
    private String productName;
    private Long totalSold;
}
