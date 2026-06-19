package com.deep.smartinventoryandordermanagementsystem.dto.dashboard;

import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RecentOrderDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="dd-MM-yyyy")
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private OrderStatus status;
}
