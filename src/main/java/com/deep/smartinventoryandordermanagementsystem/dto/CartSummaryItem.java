package com.deep.smartinventoryandordermanagementsystem.dto;

import lombok.Data;

@Data
public class CartSummaryItem {
    private int productId;
    private String productName;
    private int quantity;
    private int price;
    private int subtotal;
}
