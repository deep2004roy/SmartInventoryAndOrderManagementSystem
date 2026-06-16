package com.deep.smartinventoryandordermanagementsystem.dto;

import lombok.Data;

@Data
public class LowStockProductDto {
    private Long id;
    private String name;
    private int quantity;
}
