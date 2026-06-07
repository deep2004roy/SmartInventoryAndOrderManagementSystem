package com.deep.smartinventoryandordermanagementsystem.dto;

import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class OrderDetailsDTO {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;
    private Double totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
}
