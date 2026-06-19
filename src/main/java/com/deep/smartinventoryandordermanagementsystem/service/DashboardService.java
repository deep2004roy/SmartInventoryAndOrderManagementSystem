package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.*;
import com.deep.smartinventoryandordermanagementsystem.dto.dashboard.*;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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


    private List<RecentOrderDTO> getRecentOrders(){
        List<Order> orders = orderRepo.findTop5ByOrderByCreatedAtDesc();
        List<RecentOrderDTO> recentOrders = orders.stream().map(order -> {
            RecentOrderDTO dto = new RecentOrderDTO();
            dto.setId(order.getId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            return dto;
        }).toList();
        return recentOrders;
    }

    private List<MonthlyRevenueDTO> getMonthlyRevenue(){
        List<Object[]> results = orderRepo.getMonthlyRevenue();
        List<MonthlyRevenueDTO> monthlyRevenue = new ArrayList<>();
        for(Object[] row : results){
            Integer monthNumber = (Integer) row[0];
            BigDecimal rev = (BigDecimal) row[1];
            String month = Month.of(monthNumber).getDisplayName(TextStyle.SHORT,
                    Locale.ENGLISH);
            monthlyRevenue.add(new MonthlyRevenueDTO(month, rev));
        }
        return monthlyRevenue;
    }

    private List<TopSellingProductDTO> getTopSellingProducts(){
        List<Object[]> result = orderItemRepo.getTopSellingProducts();
        List<TopSellingProductDTO> topSellingProducts =
                result.stream().limit(5)
                        .map(row -> new TopSellingProductDTO((String) row[0],
                                ((Number) row[1]).longValue())).toList();
        return  topSellingProducts;
    }


   public DashboardSummaryDTO getSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();
        long totalProducts = productRepo.count();
        summary.setTotalProducts(totalProducts);
        long totalOrders = orderRepo.count();
        summary.setTotalOrders(totalOrders);
        Long totalStock = productRepo.getTotalStock();
        summary.setTotalStock(totalStock == null ? 0: totalStock.intValue());
        BigDecimal revenue = orderRepo.getTotalRevenue();
        summary.setRevenue(revenue == null ? BigDecimal.ZERO : revenue);
        summary.setLowStockProducts(productRepo.countLowStockProducts());
        summary.setOutOfStockProducts(productRepo.countByQuantity(0));
       BigDecimal inventoryCostValue =
               productRepo.getInventoryCostValue();

       summary.setInventoryCostValue(
               inventoryCostValue == null
                       ? BigDecimal.ZERO
                       : inventoryCostValue
       );

       BigDecimal inventorySellingValue =
               productRepo.getInventorySellingValue();

       summary.setInventorySellingValue(
               inventorySellingValue == null
                       ? BigDecimal.ZERO
                       : inventorySellingValue
       );
       summary.setPotentialProfit(
               inventorySellingValue.subtract(inventoryCostValue)
       );
        summary.setRecentOrders(getRecentOrders());
        summary.setPendingOrders(orderRepo.countByStatus(OrderStatus.PENDING));
        summary.setConfirmedOrders(orderRepo.countByStatus(OrderStatus.CONFIRMED));
        summary.setShippedOrders(orderRepo.countByStatus(OrderStatus.SHIPPED));
        summary.setDeliveredOrders(orderRepo.countByStatus(OrderStatus.DELIVERED));
        summary.setCancelledOrders(orderRepo.countByStatus(OrderStatus.CANCELLED));
        summary.setMonthlyRevenue(getMonthlyRevenue());
        summary.setTopSellingProducts(getTopSellingProducts());
        return summary;
    }
}
