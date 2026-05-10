package com.deep.smartinventoryandordermanagementsystem.dto;

import com.deep.smartinventoryandordermanagementsystem.model.Order;
import lombok.Data;

@Data
public class OrderItemRequest {
    private int productId;
    private int quantity;
}
