package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.*;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DashboardService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public DashboardService(ProductRepo productRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }


//    public  DashboardSummary getSummary() {
//        DashboardSummary summary = new DashboardSummary();
//        long totalProducts = productRepo.count();
//        summary.setTotalProducts((int)totalProducts);
//        long totalOrders = orderRepo.count();
//        summary.setTotalOrders((int)totalOrders);
//        Long totalStock =
//                productRepo.getTotalStock();
//        summary.setTotalStock(totalStock == null ? 0: totalStock.intValue());
//        Double revenue = orderRepo.getTotalRevenue();
//        summary.setRevenue(revenue == null ? 0.0 : revenue);
//        List<Product> products = productRepo.findByQuantityLessThanEqual(5);
//        List<LowStockProductDto> lowStockProducts = products.stream().map(product -> {
//            LowStockProductDto dto = new LowStockProductDto();
//            dto.setId(product.getProductId());
//            dto.setName(product.getName());
//            dto.setQuantity(product.getQuantity());
//            return dto;
//        }).toList();
//        summary.setLowStockProducts(lowStockProducts);
//        List<Order> orders = orderRepo.findTop5ByOrderByDateDesc();
//        List<RecentOrderDto> recentOrders = orders.stream().map(order -> {
//            RecentOrderDto dto = new RecentOrderDto();
//            dto.setId(order.getId());
//            dto.setDate(order.getDate());
//            dto.setTotalAmount(order.getTotalAmount());
//            dto.setStatus(order.getStatus());
//            return dto;
//        }).toList();
//        summary.setRecentOrders(recentOrders);
//        summary.setPendingOrders(orderRepo.countByStatus(OrderStatus.PENDING));
//        summary.setConfirmedOrders(orderRepo.countByStatus(OrderStatus.CONFIRMED));
//        summary.setShippedOrders(orderRepo.countByStatus(OrderStatus.SHIPPED));
//        summary.setDeliveredOrders(orderRepo.countByStatus(OrderStatus.DELIVERED));
//        summary.setCancelledOrders(orderRepo.countByStatus(OrderStatus.CANCELLED));
//        List<Object[]> results = orderRepo.getMonthlyRevenue();
//        List<MonthlyRevenueDto> monthlyRevenue = new ArrayList<>();
//        for(Object[] row : results){
//            Integer monthNumber = (Integer) row[0];
//            Double rev = (Double) row[1];
//            String month = Month.of(monthNumber).getDisplayName(TextStyle.SHORT,
//                    Locale.ENGLISH);
//            monthlyRevenue.add(new MonthlyRevenueDto(month, rev));
//        }
//        summary.setMonthlyRevenue(monthlyRevenue);
//        List<Object[]> result = orderItemRepo.getTopSellingProducts();
//        List<TopSellingProductDto> topSellingProducts =
//                result.stream().limit(5)
//                        .map(row -> new TopSellingProductDto((String) row[0],
//                                ((Number) row[1]).longValue())).toList();
//        summary.setTopSellingProducts(topSellingProducts);
//        return summary;
//    }
}
