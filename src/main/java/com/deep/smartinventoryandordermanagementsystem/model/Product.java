package com.deep.smartinventoryandordermanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(unique = true)
    private String sku; //Stock keeping unit

    private String name;
    private String description;
    private String brand;
    private String category;
    private BigDecimal costPrice;
    private BigDecimal price;
    private Integer quantity;
    private Integer reorderLevel;
    private Boolean active = true;
    private String imageUrl;

    @Column(unique = true)
    private String barcode;

    private String unit;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }



}
