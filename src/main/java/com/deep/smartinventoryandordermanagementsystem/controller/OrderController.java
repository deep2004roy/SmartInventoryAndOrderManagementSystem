package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public Order createOrder(@Valid @RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }


}
