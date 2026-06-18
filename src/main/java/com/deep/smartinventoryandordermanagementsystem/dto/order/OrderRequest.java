package com.deep.smartinventoryandordermanagementsystem.dto.order;

import com.deep.smartinventoryandordermanagementsystem.dto.orderItem.OrderItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
@Data
public class OrderRequest {
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    private String customerPhone;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String notes;

    @Valid
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequest> orderItemRequests;
}
