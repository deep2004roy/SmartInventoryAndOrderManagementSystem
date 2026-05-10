package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.OrderItemRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderItem;
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

    public Order createOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setDate(new Date());
        order.setTotalAmount(0);
        order.setStatus(true);

        int total = 0;

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemRequest> items = orderRequest.getOrderItemRequests();
        for(OrderItemRequest item : items){
            Product product1 = productRepo.findById(item.getProductId()).orElseThrow();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product1);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product1.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            product1.setQuantity(product1.getQuantity() - orderItem.getQuantity());
            productRepo.save(product1);
            total += orderItem.getPrice() * orderItem.getQuantity();
        }
        order.setTotalAmount(total);
        orderRepo.save(order);
        orderItemRepo.saveAll(orderItems);
        return order;
    }
}
