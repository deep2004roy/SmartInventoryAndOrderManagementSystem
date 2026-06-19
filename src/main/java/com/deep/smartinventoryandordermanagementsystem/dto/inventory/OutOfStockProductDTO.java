package com.deep.smartinventoryandordermanagementsystem.dto.inventory;

import lombok.Data;

@Data
public class OutOfStockProductDTO {
    private Long productId;
    private String sku;
    private String productName;
}
