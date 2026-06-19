package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderDetailsDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<OrderSummaryDTO>> getAllOrders(@RequestParam(required = false) OrderStatus status,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(status, page, size));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDetailsDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

//    @PostMapping("/cart/summary")
//    public CartSummaryResponse createCartSummary(@RequestBody OrderRequest orderRequest){
//        return orderService.createCartSummary(orderRequest);
//    }

    @PutMapping("/orders/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> changeStatus(
            @PathVariable Long id,
            @PathVariable("status") OrderStatus newStatus
    ) {
        Order updatedOrder = orderService.changeStatus(id, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/me/orders")
    public ResponseEntity<List<Order>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long id) {

        Order cancelledOrder = orderService.cancelOrder(id);

        return ResponseEntity.ok(cancelledOrder);
    }


}
