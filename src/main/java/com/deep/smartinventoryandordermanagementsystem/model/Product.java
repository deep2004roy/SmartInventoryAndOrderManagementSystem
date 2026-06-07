package com.deep.smartinventoryandordermanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank(message = "Product name required")
    private String name;
    private String description;
    @Positive(message = "Price must be positive")
    private Double price;
    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;
    private String category;
    private Boolean active;
    private String imageUrl;
}
