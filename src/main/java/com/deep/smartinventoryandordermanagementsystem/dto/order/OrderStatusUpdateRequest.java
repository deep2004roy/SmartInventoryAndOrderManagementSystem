package com.deep.smartinventoryandordermanagementsystem.dto.order;

import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private OrderStatus status;
}
