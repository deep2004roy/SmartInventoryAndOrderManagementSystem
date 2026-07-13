package com.deep.smartinventoryandordermanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantityChanged; // +10 or -5

    private Integer quantityAfter; // stock after movement

    @Enumerated(EnumType.STRING)
    private StockMovementType type;

    private String reference;
    // e.g. ORDER-1023, PURCHASE-55

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
