package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderDetailsDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderItemDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.order.OrderSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.orderItem.OrderItemRequest;
import com.deep.smartinventoryandordermanagementsystem.exception.*;
import com.deep.smartinventoryandordermanagementsystem.exception.IllegalArgumentException;
import com.deep.smartinventoryandordermanagementsystem.model.*;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final StockMovementService stockMovementService;

    public OrderService(OrderRepo orderRepo,
                        ProductRepo productRepo,
                        OrderItemRepo orderItemRepo,
                        UserRepo userRepo,
                        StockMovementService stockMovementService) {

        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.stockMovementService = stockMovementService;
    }

    private OrderSummaryDTO mapToOrderSummaryDTO(Order order) {

        OrderSummaryDTO dto = new OrderSummaryDTO();

        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerName(order.getCustomerName());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());

        return dto;
    }

    private OrderDetailsDTO mapToOrderDetailsDTO(Order order) {

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

        List<OrderItemDTO> items = order.getOrderItems()
                .stream()
                .map(item -> {

                    OrderItemDTO itemDto =
                            new OrderItemDTO();

                    itemDto.setId(item.getId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setProductSku(item.getProductSku());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    itemDto.setSubtotal(item.getSubtotal());

                    return itemDto;
                })
                .toList();

        dto.setOrderItems(items);

        return dto;
    }

    private User getCurrentUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return Optional.ofNullable(
                userRepo.findUserByUsername(username)
        ).orElseThrow(() ->
                new UserNotFoundException(
                        "User not found"
                ));
    }


    private Order getOrderEntity(Long id) {

        return orderRepo.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found: " + id
                        ));
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {

        return switch (current) {
            case PENDING -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED -> next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
            case SHIPPED -> next == OrderStatus.DELIVERED;
            case DELIVERED -> false;
            case CANCELLED -> false;
        };
    }

    public OrderSummaryDTO  changeStatus(Long id, OrderStatus newStatus) {

        Order order = getOrderEntity(id);
        OrderStatus current = order.getStatus();
        if (!isValidTransition(current, newStatus)) {
            throw new InvalidOrderStatusException("Cannot change from " + current + " to " + newStatus);
        }
        order.setStatus(newStatus);
        Order savedOrder = orderRepo.save(order);

        return mapToOrderSummaryDTO(savedOrder);
    }

    @Transactional
    public OrderSummaryDTO  createOrder(OrderRequest orderRequest){
        if(orderRequest.getOrderItemRequests().isEmpty()){
            throw new IllegalArgumentException(
                    "Order must contain at least one item"
            );
        }
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerPhone(orderRequest.getCustomerPhone());
        User currentUser = getCurrentUser();

        order.setUser(currentUser);
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setNotes(orderRequest.getNotes());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        order.setOrderNumber(
                "ORD-" + System.currentTimeMillis()
        );

        for(OrderItemRequest item : orderRequest.getOrderItemRequests()){
            //fetch product
            Product product = productRepo.findById(item.getProductId()).orElseThrow(() ->
                    new ProductNotFoundException("Product not found " + item.getProductId()));

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new ProductInactiveException(
                        "Product is inactive"
                );
            }

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
            orderItem.setCostPrice(product.getCostPrice());
            orderItem.setOrder(order);
            orderItem.setSubtotal(subtotal);
            orderItem.setProductName(product.getName());
            orderItem.setProductSku(product.getSku());

            orderItems.add(orderItem);

            totalAmount = totalAmount.add(subtotal);

            //reduce stock
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepo.save(product);

            stockMovementService.recordMovement(
                    product,
                    -item.getQuantity(),
                    StockMovementType.ORDER,
                    order.getOrderNumber()
            );

        }
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        return mapToOrderSummaryDTO(savedOrder);
    }

    public Page<OrderSummaryDTO> getAllOrders(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders;
        if (status != null) {
            orders = orderRepo.findByStatus(status, pageable);
        }else{
            orders = orderRepo.findAll(pageable);
        }
        return orders.map(this::mapToOrderSummaryDTO);
    }

    private boolean canViewAllOrders(User user) {
        return user.getRole() == Role.ADMIN
                || user.getRole() == Role.MANAGER
                || user.getRole() == Role.STAFF;
    }

    public OrderDetailsDTO getOrderById(Long id) {

        Order order = getOrderEntity(id);
        User currentUser = getCurrentUser();

        if (!canViewAllOrders(currentUser)
                && !order.getUser().getId().equals(currentUser.getId())) {

            throw new AccessDeniedException(
                    "You are not allowed to access this order"
            );
        }

        return mapToOrderDetailsDTO(order);
    }

    private static final List<String> ALLOWED_ORDER_SORT_FIELDS = List.of(
            "createdAt",
            "totalAmount",
            "status",
            "customerName",
            "orderNumber"
    );

    private Sort buildSort(String sort) {

        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }

        String[] values = sort.split(",");

        if (values.length != 2) {
            throw new InvalidSortParameterException(
                    "Sort format should be field,direction"
            );
        }

        String field = values[0].trim();
        String direction = values[1].trim();

        if (!ALLOWED_ORDER_SORT_FIELDS.contains(field)) {
            throw new InvalidSortParameterException(
                    "Invalid sort field: " + field
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new InvalidSortParameterException(
                    "Sort direction must be asc or desc"
            );
        }

        return direction.equalsIgnoreCase("asc")
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
    }

    public Page<OrderSummaryDTO> getOrders(
            int page,
            int size,
            String search,
            OrderStatus status,
            String sort
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                buildSort(sort)
        );

        return orderRepo
                .findOrders(
                        search,
                        status,
                        pageable
                )
                .map(this::mapToOrderSummaryDTO);
    }

    public Page<OrderSummaryDTO> getMyOrders(
            int page,
            int size,
            OrderStatus status,
            String sort
    ) {

        User currentUser = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                buildSort(sort)
        );

        Page<Order> orders;

        if (status != null) {

            orders = orderRepo.findByUserAndStatus(
                    currentUser,
                    status,
                    pageable
            );

        } else {

            orders = orderRepo.findByUser(
                    currentUser,
                    pageable
            );
        }

        return orders.map(this::mapToOrderSummaryDTO);
    }

    @Transactional
    public OrderSummaryDTO cancelOrder(Long orderId) {

        Order order = getOrderEntity(orderId);
        User currentUser = getCurrentUser();

        boolean isAdmin =
                currentUser.getRole() == Role.ADMIN;

        if (!isAdmin &&
                !order.getUser().getId()
                        .equals(currentUser.getId())) {

            throw new AccessDeniedException(
                    "You cannot cancel this order"
            );
        }


        if (order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {

            throw new InvalidOrderStatusException(
                    "Shipped or delivered orders cannot be cancelled");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException(
                    "Order is already cancelled");
        }

        // Restore stock
        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            product.setQuantity(
                    product.getQuantity() + item.getQuantity()
            );

            productRepo.save(product);

            stockMovementService.recordMovement(
                    product,
                    item.getQuantity(),
                    StockMovementType.RETURN,
                    order.getOrderNumber()
            );

        }

        order.setStatus(OrderStatus.CANCELLED);

        Order savedOrder = orderRepo.save(order);

        return mapToOrderSummaryDTO(savedOrder);
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
