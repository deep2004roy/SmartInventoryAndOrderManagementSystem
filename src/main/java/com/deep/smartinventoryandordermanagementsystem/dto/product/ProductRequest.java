package com.deep.smartinventoryandordermanagementsystem.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Category is required")
    private String category;

    @Positive(message = "Cost price must be positive")
    private Double costPrice;

    @Positive(message = "Selling price must be positive")
    private Double price;

    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;

    @PositiveOrZero(message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    @NotBlank(message = "Barcode is required")
    private String barcode;

    @NotBlank(message = "Unit is required")
    private String unit;

    private Boolean active = true;
}
