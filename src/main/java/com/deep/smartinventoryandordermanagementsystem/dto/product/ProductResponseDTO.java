package com.deep.smartinventoryandordermanagementsystem.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String sku;
    private String name;
    private String description;
    private String brand;
    private String category;
    private BigDecimal costPrice;
    private BigDecimal price;
    private Integer quantity;
    private Integer reorderLevel;
    private Boolean active;
    private String barcode;
    private String unit;
    private String imageUrl;
}
