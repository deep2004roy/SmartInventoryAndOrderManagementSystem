package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.CartSummaryResponse;
import com.deep.smartinventoryandordermanagementsystem.dto.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable int id){
        return orderService.getProductById(id);
    }

    @PostMapping("/cart/summary")
    public CartSummaryResponse createCartSummary(@RequestBody OrderRequest orderRequest){
        return orderService.createCartSummary(orderRequest);
    }

    @PutMapping("/orders/{id}/status/{status}")
    public Order changeStatus(@PathVariable int id, @PathVariable OrderStatus status){
        return orderService.changeStatus(id, status);
    }


}
