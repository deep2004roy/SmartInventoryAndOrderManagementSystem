package com.deep.smartinventoryandordermanagementsystem.dto.order;

import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderDetailsDTO {
    private Long id;
    private String orderNumber;

    private String customerName;
    private String customerPhone;
    private String shippingAddress;
    private String paymentMethod;

    private BigDecimal totalAmount;
    private OrderStatus status;
    private String notes;

    private LocalDateTime createdAt;

    private List<OrderItemDTO> orderItems;
}
