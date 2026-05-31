package com.deep.smartinventoryandordermanagementsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull
    private Long productId;
    @PositiveOrZero
    private int quantity;
}
