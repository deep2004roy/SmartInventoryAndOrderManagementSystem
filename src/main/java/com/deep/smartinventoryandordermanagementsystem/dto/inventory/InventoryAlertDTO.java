package com.deep.smartinventoryandordermanagementsystem.dto.inventory;

import lombok.Data;

@Data
public class InventoryAlertDTO {
    private Long productId;
    private String sku;
    private String productName;
    private Integer currentStock;
    private Integer reorderLevel;
}
