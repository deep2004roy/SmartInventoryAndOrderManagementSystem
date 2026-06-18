package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderDetailsDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderItemDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.orderItem.OrderItemRequest;
import com.deep.smartinventoryandordermanagementsystem.exception.InsufficientStockException;
import com.deep.smartinventoryandordermanagementsystem.exception.ProductNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderItem;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final OrderItemRepo orderItemRepo;

    public OrderService(OrderRepo orderRepo, ProductRepo productRepo, List<OrderItem> orderItems, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.orderItemRepo = orderItemRepo;
    }

//    public Order changeStatus(int id, OrderStatus status) {
//        Order order = orderRepo.findById(id).orElseThrow();
//        order.setStatus(status);
//        return orderRepo.save(order);
//    }

    @Transactional
    public Order createOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerPhone(orderRequest.getCustomerPhone());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setNotes(orderRequest.getNotes());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequest item : orderRequest.getOrderItemRequests()){
            //fetch product
            Product product = productRepo.findById(item.getProductId()).orElseThrow(() ->
                    new ProductNotFoundException("Product not found" + item.getProductId()));

            //validate stock before processing
            if (item.getQuantity() > product.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for product: " + product.getName()
                );
            }


            // price as BigDecimal
            BigDecimal price = product.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal subtotal = price.multiply(quantity);

            //create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);
            orderItem.setSubtotal(subtotal);
            orderItem.setProductName(product.getName());
            orderItem.setProductSku(product.getSku());

            orderItems.add(orderItem);

            totalAmount = totalAmount.add(subtotal);

            //reduce stock
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepo.save(product);

        }
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        return orderRepo.save(order);
    }

    public Page<OrderSummaryDTO> getAllOrders(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders;
        if (status != null) {
            orders = orderRepo.findByStatus(status, pageable);
        }else{
            orders = orderRepo.findAll(pageable);
        }
        return orders.map(order -> {
            OrderSummaryDTO dto = new OrderSummaryDTO();
            dto.setId(order.getId());
            dto.setOrderNumber(order.getOrderNumber());
            dto.setCustomerName(order.getCustomerName());
            dto.setStatus(order.getStatus());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setCreatedAt(order.getCreatedAt());
            return dto;
        });
    }

    public OrderDetailsDTO getOrderById(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        OrderDetailsDTO dto = new OrderDetailsDTO();

        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerPhone(order.getCustomerPhone());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(item -> {
                    OrderItemDTO i = new OrderItemDTO();
                    i.setId(item.getId());
                    i.setProductName(item.getProductName());
                    i.setProductSku(item.getProductSku());
                    i.setQuantity(item.getQuantity());
                    i.setPrice(item.getPrice());
                    i.setSubtotal(item.getSubtotal());
                    return i;
                }).toList();
        dto.setOrderItems(items);

        return dto;
    }

//    public CartSummaryResponse createCartSummary(OrderRequest orderRequest) {
//        List<OrderItemRequest> items = orderRequest.getOrderItemRequests();
//        CartSummaryResponse summaryResponse = new CartSummaryResponse();
//        List<CartSummaryItem> summaryItems = new ArrayList<>();
//        double totalAmount = 0.00;
//        int totalItems = 0;
//        for(OrderItemRequest item : items){
//            CartSummaryItem summaryItem = new CartSummaryItem();
//            Product product = productRepo.findById(item
//                    .getProductId()).orElseThrow(() -> new ProductNotFoundException("No such product"));
//            double subTotal = product.getPrice() * item.getQuantity();
//            totalAmount += subTotal;
//            totalItems += item.getQuantity();
//            summaryItem.setProductId(product.getProductId());
//            summaryItem.setProductName(product.getName());
//            summaryItem.setPrice(product.getPrice());
//            summaryItem.setQuantity(item.getQuantity());
//            summaryItem.setSubtotal(subTotal);
//            summaryItem.setImageUrl(product.getImageUrl());
//            summaryItems.add(summaryItem);
//        }
//        summaryResponse.setTotalItems(totalItems);
//        summaryResponse.setTotalAmount(totalAmount);
//        summaryResponse.setCartSummaryItems(summaryItems);
//        return summaryResponse;
//
//    }
}
