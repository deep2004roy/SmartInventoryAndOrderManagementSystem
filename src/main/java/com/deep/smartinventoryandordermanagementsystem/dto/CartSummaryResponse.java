package com.deep.smartinventoryandordermanagementsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartSummaryResponse {
    private int totalItems;
    private int totalAmount;
    private List<CartSummaryItem> cartSummaryItems;
}
