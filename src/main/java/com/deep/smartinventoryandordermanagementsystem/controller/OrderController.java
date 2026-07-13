package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderDetailsDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderStatusUpdateRequest;
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
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<OrderSummaryDTO> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        OrderSummaryDTO order = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<Page<OrderSummaryDTO>> getAllOrders(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,

            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String sort
    ) {

        return ResponseEntity.ok(
                orderService.getOrders(
                        page,
                        size,
                        search,
                        status,
                        sort
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDetailsDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

//    @PostMapping("/cart/summary")
//    public CartSummaryResponse createCartSummary(@RequestBody OrderRequest orderRequest){
//        return orderService.createCartSummary(orderRequest);
//    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<OrderSummaryDTO> changeStatus(
            @PathVariable Long id,
            @RequestBody OrderStatusUpdateRequest request
    ) {
        OrderSummaryDTO  updatedOrder = orderService.changeStatus(id, request.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<OrderSummaryDTO>> getMyOrders(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String sort
    ) {

        return ResponseEntity.ok(
                orderService.getMyOrders(
                        page,
                        size,
                        status,
                        sort
                )
        );
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<OrderSummaryDTO> cancelOrder(
            @PathVariable Long id) {

        OrderSummaryDTO cancelledOrder = orderService.cancelOrder(id);

        return ResponseEntity.ok(cancelledOrder);
    }


}
