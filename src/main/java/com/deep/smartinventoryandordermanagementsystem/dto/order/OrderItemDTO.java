package com.deep.smartinventoryandordermanagementsystem.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private String productName;
    private String productSku;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
