package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.controller.ProductController;
import com.deep.smartinventoryandordermanagementsystem.dto.CartSummaryItem;
import com.deep.smartinventoryandordermanagementsystem.dto.CartSummaryResponse;
import com.deep.smartinventoryandordermanagementsystem.dto.OrderItemRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.exception.InsufficientStockException;
import com.deep.smartinventoryandordermanagementsystem.exception.ProductNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderItem;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public Order changeStatus(int id, OrderStatus status) {
        Order order = orderRepo.findById(id).orElseThrow();
        order.setStatus(status);
        return orderRepo.save(order);
    }

    public Order createOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setDate(new Date());
        order.setTotalAmount(0);
        order.setStatus(OrderStatus.PENDING);

        int total = 0;

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemRequest> items = orderRequest.getOrderItemRequests();
        for(OrderItemRequest item : items){
            Product product1 = productRepo.findById(item.getProductId()).orElseThrow(() ->
                    new ProductNotFoundException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product1);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product1.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            if (orderItem.getQuantity() > product1.getQuantity()){
                throw new InsufficientStockException(STR."Not enough stock for product: \{product1.getName()}");
            }

            product1.setQuantity(product1.getQuantity() - orderItem.getQuantity());
            productRepo.save(product1);
            total += orderItem.getPrice() * orderItem.getQuantity();
        }
        order.setTotalAmount(total);
        orderRepo.save(order);
        orderItemRepo.saveAll(orderItems);
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getProductById(int id) {
        return orderRepo.findById(id).orElseThrow();
    }

    public CartSummaryResponse createCartSummary(OrderRequest orderRequest) {
        List<OrderItemRequest> items = orderRequest.getOrderItemRequests();
        CartSummaryResponse summaryResponse = new CartSummaryResponse();
        List<CartSummaryItem> summaryItems = new ArrayList<>();
        int totalAmount = 0;
        int totalItems = 0;
        for(OrderItemRequest item : items){
            CartSummaryItem summaryItem = new CartSummaryItem();
            Product product = productRepo.findById(item
                    .getProductId()).orElseThrow(() -> new ProductNotFoundException("No such product"));
            Double subTotal = product.getPrice() * item.getQuantity();
            totalAmount += subTotal;
            totalItems += item.getQuantity();
            summaryItem.setProductId(product.getId());
            summaryItem.setProductName(product.getName());
            summaryItem.setPrice(product.getPrice());
            summaryItem.setQuantity(item.getQuantity());
            summaryItem.setSubtotal(subTotal);
            summaryItems.add(summaryItem);
        }
        summaryResponse.setTotalItems(totalItems);
        summaryResponse.setTotalAmount(totalAmount);
        summaryResponse.setCartSummaryItems(summaryItems);
        return summaryResponse;

    }
}
