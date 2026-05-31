package com.deep.smartinventoryandordermanagementsystem.dto;

import lombok.Data;

@Data
public class CartSummaryItem {
    private Long productId;
    private String productName;
    private int quantity;
    private Double price;
    private Double subtotal;
}
