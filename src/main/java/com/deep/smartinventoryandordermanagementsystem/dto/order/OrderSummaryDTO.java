package com.deep.smartinventoryandordermanagementsystem.dto.order;

import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class OrderSummaryDTO {
    private Long id;
    private String orderNumber;
    private String customerName;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}
